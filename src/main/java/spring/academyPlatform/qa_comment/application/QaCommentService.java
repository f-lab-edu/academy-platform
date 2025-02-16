package spring.academyPlatform.qa_comment.application;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.qa_comment.dao.QaCommentRepository;
import spring.academyPlatform.qa_comment.domain.QaComment;
import spring.academyPlatform.qa_comment.dto.QaCommentCreateRequest;
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;
import spring.academyPlatform.qa_comment.mapper.QaCommentMapper;

@Service
@RequiredArgsConstructor
public class QaCommentService {
	private final QaCommentRepository qaCommentRepository;
	private final QaCommentMapper qaCommentMapper;

	@Transactional
	public QaCommentResponse createComment(QaCommentCreateRequest requestDto, HttpSession session) {
		long priorityNumber;

		if (requestDto.getParentCommentId() != null) {
			// 대댓글 생성인 경우: 부모 댓글의 자식들 중 최대 priorityNumber + 1
			List<QaComment> childComments = qaCommentRepository.findByParentsCommentIdAndDeletedYn(
				requestDto.getParentCommentId(), YnCode.N);
			long maxPriority = childComments.stream()
				.map(QaComment::getPriorityNumber)
				.filter(Objects::nonNull)
				.max(Comparator.naturalOrder())
				.orElse(Long.valueOf(0));
			priorityNumber = maxPriority + 1;
		} else {
			// 최상위 댓글 생성인 경우: 해당 게시글의 최상위 댓글 중 최대 priorityNumber + 1
			List<QaComment> topComments = qaCommentRepository.findByBoardIdAndParentsCommentIdIsNullAndDeletedYn(
				requestDto.getBoardId(),
				YnCode.N);
			long maxPriority = topComments.stream()
				.map(QaComment::getPriorityNumber)
				.filter(Objects::nonNull)
				.max(Comparator.naturalOrder())
				.orElse(Long.valueOf(0));
			priorityNumber = maxPriority + 1;
		}

		// 빌더 패턴을 사용하여 Comment 생성
		QaComment comment = QaComment.builder()
			.boardId(requestDto.getBoardId())
			.userId(session.getAttribute("userId").toString())
			.parentsCommentId(requestDto.getParentCommentId())  // null이면 최상위 댓글
			.title(requestDto.getTitle())
			.post(requestDto.getPost())
			.createdBy(session.getAttribute("user").toString())
			.priorityNumber(priorityNumber)
			.build();

		qaCommentRepository.save(comment);

		return qaCommentMapper.change(comment);
	}

}
