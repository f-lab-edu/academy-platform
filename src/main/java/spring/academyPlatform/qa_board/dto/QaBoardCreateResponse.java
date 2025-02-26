package spring.academyPlatform.qa_board.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;

@Getter
public class QaBoardCreateResponse {
	private Long boardId;
	private String userId;
	private String title;
	private String post;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
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
