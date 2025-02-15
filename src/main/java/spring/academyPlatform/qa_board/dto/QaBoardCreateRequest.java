package spring.academyPlatform.qa_board.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class QaBoardCreateRequest {

	@NotNull
	private String title;
	@NotNull
	private String post;

	@Builder(toBuilder = true)
	public QaBoardCreateRequest(String title, String post) {
		this.title = title;
		this.post = post;
	}
}
