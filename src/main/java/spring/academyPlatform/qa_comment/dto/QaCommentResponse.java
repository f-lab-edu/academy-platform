package spring.academyPlatform.qa_comment.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import spring.academyPlatform.global.model.YnCode;

@Getter
@Setter
@ToString
public class QaCommentResponse {

	private Long commentId;
	private Long boardId;
	private String userId;
	private Long parentsCommentId;
	private Long priorityNumber;
	private String title;
	private String post;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;
	private YnCode deletedYn;

	private List<QaCommentResponse> children = new ArrayList<>();

	@Builder
	public QaCommentResponse(Long commentId, Long boardId, String userId, Long parentsCommentId, Long priorityNumber,
		String title, String post, LocalDateTime createdAt, LocalDateTime modifiedAt, YnCode deletedYn,
		List<QaCommentResponse> children) {
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
		this.children = (children == null) ? new ArrayList<>() : children;
	}

}
