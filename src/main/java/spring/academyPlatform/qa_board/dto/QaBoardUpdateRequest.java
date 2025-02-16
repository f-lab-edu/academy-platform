package spring.academyPlatform.qa_board.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QaBoardUpdateRequest {

	private String title;
	private String post;

	@Builder
	public QaBoardUpdateRequest(String title, String post) {
		this.title = title;
		this.post = post;
	}
}
