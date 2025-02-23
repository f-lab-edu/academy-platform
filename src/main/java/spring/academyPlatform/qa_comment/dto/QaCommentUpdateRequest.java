package spring.academyPlatform.qa_comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QaCommentUpdateRequest {
	private String title;
	private String post;

	@Builder
	public QaCommentUpdateRequest(String title, String post) {
		this.title = title;
		this.post = post;
	}
}
