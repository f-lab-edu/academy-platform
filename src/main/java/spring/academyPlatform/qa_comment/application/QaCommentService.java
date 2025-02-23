package spring.academyPlatform.qa_comment.application;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.qa_comment.dao.QaCommentRepository;
import spring.academyPlatform.qa_comment.domain.QaComment;
import spring.academyPlatform.qa_comment.dto.QaCommentCreateRequest;
import spring.academyPlatform.qa_comment.dto.QaCommentCreateResponse;
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;
import spring.academyPlatform.qa_comment.dto.QaCommentUpdateRequest;
import spring.academyPlatform.qa_comment.mapper.QaCommentMapper;

@Service
@RequiredArgsConstructor
public class QaCommentService {
	private final QaCommentRepository qaCommentRepository;
	private final QaCommentMapper qaCommentMapper;

	@Transactional
	public QaCommentCreateResponse createComment(QaCommentCreateRequest requestDto, HttpSession session) {
		final long PRIORITY_NUMBER;

		if (requestDto.getParentCommentId() != null) {
			// 대댓글 생성인 경우: 부모 댓글의 자식들 중 최대 priorityNumber + 1
			Long maxPriority = qaCommentRepository.findMaxPriorityByParentCommentId(
				requestDto.getParentCommentId(), YnCode.N);
			PRIORITY_NUMBER = getNextPriorityNumber(maxPriority);

		} else {
			// 최상위 댓글 생성인 경우: 해당 게시글의 최상위 댓글 중 최대 priorityNumber + 1
			Long maxPriority = qaCommentRepository.findMaxPriorityByBoardIdAndParentsCommentIdIsNull(
				requestDto.getBoardId(), YnCode.N);
			PRIORITY_NUMBER = getNextPriorityNumber(maxPriority);
		}

		// 빌더 패턴을 사용하여 Comment 생성
		QaComment comment = QaComment.builder()
			.boardId(requestDto.getBoardId())
			.userId(session.getAttribute("userId").toString())
			.parentsCommentId(requestDto.getParentCommentId())  // null이면 최상위 댓글
			.title(requestDto.getTitle())
			.post(requestDto.getPost())
			.createdBy(session.getAttribute("user").toString())
			.priorityNumber(PRIORITY_NUMBER)
			.build();

		qaCommentRepository.save(comment);

		return qaCommentMapper.toCreateDto(comment);
	}

	private long getNextPriorityNumber(Long maxPriority) {
		return (maxPriority == null ? 0L : maxPriority) + 1;
	}

	@Transactional
	public QaCommentResponse updateComment(Long commentId, QaCommentUpdateRequest request, HttpSession session) {
		// 댓글의 아이디를 받아서 조회 후 수정작업을 진행한다.
		// 수정 대상은 댓글의 제목과 본문
		// 수정자 , 수정일시 들어가는지 확인하기
		QaComment comment = qaCommentRepository.findByCommentIdAndDeletedYn(commentId, YnCode.N)
			.orElseThrow(() -> new IllegalStateException("Comment not found"));
		QaComment changeComment = comment.toBuilder()
			.title(request.getTitle())
			.post(request.getPost())
			.modifiedBy(session.getAttribute("userId").toString())
			.build();

		QaComment result = qaCommentRepository.save(changeComment);

		return qaCommentMapper.change(result);
	}

	public boolean deleteComment(Long commentId) {
		// 댓글 삭제시 하위 댓글도 모두 삭제 처리가 진행되어야 한다.
		// 1. 최상위 댓글 삭제시, 모든 하위 댓글 삭제
		// 2. 대댓글 삭제시, 대댓글 하위 댓글 삭제  이런식으로...
		// parentsCommentId 조회 후 삭제하기
		// 입력 받는 commentId 값은 commentId
		// commentId 와 parentsCommentId는 동일하니 이 id 포함된 댓글 전부 삭제하기
		QaComment comment = qaCommentRepository.findByCommentIdAndDeletedYn(commentId, YnCode.N)
			.orElseThrow(() -> new IllegalStateException("Comment not found"));

		QaComment deleteComment = comment.toBuilder()
			.deletedYn(YnCode.Y)
			.build();

		qaCommentRepository.save(deleteComment);

		List<QaComment> comments = qaCommentRepository.findByParentsCommentIdAndDeletedYn(commentId, YnCode.N);
		List<QaComment> commentsToDelete = comments.stream()
			.map(underComment -> underComment.toBuilder()
				.deletedYn(YnCode.Y)
				.build())
			.toList();

		qaCommentRepository.saveAll(commentsToDelete);

		return true;
	}
}
