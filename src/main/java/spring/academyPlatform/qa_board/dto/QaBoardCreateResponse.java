package spring.academyPlatform.qa_board.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;

@Getter
public class QaBoardCreateResponse {
	private Long boardId;
	private String userId;
	private String title;
	private String post;
	private LocalDateTime createdAt;
	private LocalDateTime modifiedAt;
	private String createdBy;
	private String modifiedBy;
	private YnCode deletedYn;

	@Builder(toBuilder = true)
	public QaBoardCreateResponse(Long boardId, String userId, String title, String post, LocalDateTime createdAt,
		LocalDateTime modifiedAt, String createdBy, String modifiedBy, YnCode deletedYn) {
		this.boardId = boardId;
		this.userId = userId;
		this.title = title;
		this.post = post;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.deletedYn = deletedYn;
	}
}
