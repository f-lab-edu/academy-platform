package spring.academyPlatform.qa_board.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;

@Getter
public class QaBoardDeleteResponse {

	private Long boardId;
	private String userId;
	private String title;
	private String post;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;
	private String modifiedBy;
	private YnCode deletedYn;

	@Builder
	public QaBoardDeleteResponse(YnCode deletedYn, String modifiedBy, LocalDateTime modifiedAt,
		String post, String title, String userId, Long boardId) {
		this.deletedYn = deletedYn;
		this.modifiedBy = modifiedBy;
		this.modifiedAt = modifiedAt;
		this.post = post;
		this.title = title;
		this.userId = userId;
		this.boardId = boardId;
	}
}
