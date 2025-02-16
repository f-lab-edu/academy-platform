package spring.academyPlatform.qa_comment.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class QaCommentCreateRequest {
	private Long boardId;          // 댓글이 속한 게시글 ID
	// 부모 댓글 ID (최상위 댓글인 경우 null)
	private Long parentCommentId;
	private String title;
	private String post;

	@Builder
	public QaCommentCreateRequest(Long boardId, String userId, Long parentCommentId, String title, String post) {
		this.boardId = boardId;
		this.parentCommentId = parentCommentId;
		this.title = title;
		this.post = post;
	}
}
