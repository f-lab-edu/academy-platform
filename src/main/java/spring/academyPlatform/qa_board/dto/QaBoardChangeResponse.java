package spring.academyPlatform.qa_board.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;

@Getter
public class QaBoardChangeResponse {

	private Long boardId;
	private String userId;
	private String title;
	private String post;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;
	private String createdBy;
	private String modifiedBy;
	private YnCode deletedYn;
	private List<QaCommentResponse> comments;

	@Builder
	public QaBoardChangeResponse(Long boardId, String userId, String title, String post, LocalDateTime createdAt,
		LocalDateTime modifiedAt, String createdBy, String modifiedBy, YnCode deletedYn,
		List<QaCommentResponse> comments) {
		this.boardId = boardId;
		this.userId = userId;
		this.title = title;
		this.post = post;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.deletedYn = deletedYn;
		this.comments = comments == null ? new ArrayList<>() : comments;
	}
}
