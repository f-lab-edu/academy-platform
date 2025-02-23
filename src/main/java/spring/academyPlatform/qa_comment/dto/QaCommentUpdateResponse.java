package spring.academyPlatform.qa_comment.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;

@Getter
public class QaCommentUpdateResponse {

	private Long commentId;
	private Long boardId;
	private String userId;
	private Long parentsCommentId;
	private Long priorityNumber;
	private String title;
	private String post;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private YnCode deletedYn;

	@Builder
	public QaCommentUpdateResponse(Long commentId, Long boardId, String userId, Long parentsCommentId,
		Long priorityNumber,
		String title, String post, LocalDateTime createdAt, LocalDateTime modifiedAt, YnCode deletedYn) {
		this.commentId = commentId;
		this.boardId = boardId;
		this.userId = userId;
		this.parentsCommentId = parentsCommentId;
		this.priorityNumber = priorityNumber;
		this.title = title;
		this.post = post;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.deletedYn = deletedYn;
	}
}
