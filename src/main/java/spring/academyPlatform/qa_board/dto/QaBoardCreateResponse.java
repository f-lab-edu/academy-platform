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
	private String createdBy;
	private YnCode deletedYn;

	@Builder(toBuilder = true)
	public QaBoardCreateResponse(Long boardId, String userId, String title, String post, LocalDateTime createdAt,
		String createdBy, YnCode deletedYn) {
		this.boardId = boardId;
		this.userId = userId;
		this.title = title;
		this.post = post;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.deletedYn = deletedYn;
	}
}
