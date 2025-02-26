package spring.academyPlatform.qa_comment.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;

@Getter
public class QaCommentDeleteResponse {
	private Long commentId;
	private Long boardId;
	private String userId;
	private Long parentsCommentId;
	private Long priorityNumber;
	private String title;
	private String post;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	private String createdBy;
	private String modifiedBy;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;
	private YnCode deletedYn;

	@Builder
	public QaCommentDeleteResponse(Long commentId, Long boardId, String userId, Long parentsCommentId,
		Long priorityNumber,
		String title, String post, LocalDateTime createdAt, String createdBy, String modifiedBy,
		LocalDateTime modifiedAt,
		YnCode deletedYn) {
		this.commentId = commentId;
		this.boardId = boardId;
		this.userId = userId;
		this.parentsCommentId = parentsCommentId;
		this.priorityNumber = priorityNumber;
		this.title = title;
		this.post = post;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.modifiedAt = modifiedAt;
		this.deletedYn = deletedYn;
	}

}
