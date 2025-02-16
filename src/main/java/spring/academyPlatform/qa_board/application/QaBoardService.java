package spring.academyPlatform.qa_board.application;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.global.util.CustomPage;
import spring.academyPlatform.global.util.DateTimeFormatterUtil;
import spring.academyPlatform.qa_board.dao.QaBoardRepository;
import spring.academyPlatform.qa_board.domain.QaBoard;
import spring.academyPlatform.qa_board.dto.QaBoardChangeResponse;
import spring.academyPlatform.qa_board.dto.QaBoardCreateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardCreateResponse;
import spring.academyPlatform.qa_board.dto.QaBoardSearchResponse;
import spring.academyPlatform.qa_board.dto.QaBoardUpdateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardUpdateResponse;
import spring.academyPlatform.qa_board.mapper.QaBoardMapper;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;

@Service
@RequiredArgsConstructor
@Slf4j
public class QaBoardService {

	private final QaBoardRepository qaBoardRepository;
	private final QaBoardMapper qaBoardMapper;
	private final UserRepository userRepository;

	@Transactional
	public QaBoardCreateResponse insertBoard(QaBoardCreateRequest request, HttpSession session) {

		User user = userRepository.findByUserName(session.getAttribute("user").toString());
		String userId = user.getUserId();
		String userName = user.getUserName();

		QaBoardCreateResponse response = qaBoardMapper.changeDto(request);
		QaBoardCreateResponse result = response.toBuilder()
			.userId(userId)
			.createdBy(userName)
			.build();

		QaBoard board = qaBoardRepository.save(qaBoardMapper.toEntity(result));

		return qaBoardMapper.changeDto(board);
	}

	@Transactional(readOnly = true)
	public CustomPage<QaBoardChangeResponse> findBoard(Long boardId, String title, String userId, String startDate,
		String endDate, int page,
		int size) {

		LocalDateTime start = DateTimeFormatterUtil.parse(startDate).atStartOfDay(); // 날짜 범위 시작일
		LocalDateTime end = DateTimeFormatterUtil.parse(endDate).atTime(LocalTime.MAX);

		return qaBoardRepository.findBoard(boardId, title, userId, start, end, page, size);
	}

	@Transactional(readOnly = true)
	public QaBoardSearchResponse findSingleBoard(Long boardId) {
		QaBoard board = qaBoardRepository.findByBoardIdAndDeletedYn(boardId, YnCode.N);
		return qaBoardMapper.changeSearchResponse(board);
	}

	@Transactional
	public QaBoardUpdateResponse changeBoard(Long boardId, QaBoardUpdateRequest dto, HttpSession session) {
		QaBoard board = qaBoardRepository.findByBoardIdAndDeletedYn(boardId, YnCode.N);
		if (board == null) {
			throw new IllegalStateException("Board not found");
		}
		QaBoard changeBoard = board.toBuilder()
			.title(dto.getTitle())
			.post(dto.getPost())
			.modifiedBy(session.getAttribute("user").toString())
			.build();
		qaBoardRepository.save(changeBoard);

		return qaBoardMapper.updateDto(changeBoard);
	}

	@Transactional
	public boolean deletedBoard(Long boardId) {
		QaBoard board = qaBoardRepository.findByBoardIdAndDeletedYn(boardId, YnCode.N);
		if (board == null) {
			throw new IllegalStateException("Board not found");
		}
		QaBoard changeBoard = board.toBuilder()
			.deletedYn(YnCode.Y)
			.build();
		qaBoardRepository.save(changeBoard);

		return true;
	}
}
