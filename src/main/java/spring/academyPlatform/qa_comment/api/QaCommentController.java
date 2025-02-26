package spring.academyPlatform.qa_comment.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.qa_comment.application.QaCommentService;
import spring.academyPlatform.qa_comment.dto.QaCommentCreateRequest;
import spring.academyPlatform.qa_comment.dto.QaCommentCreateResponse;
import spring.academyPlatform.qa_comment.dto.QaCommentDeleteResponse;
import spring.academyPlatform.qa_comment.dto.QaCommentUpdateRequest;
import spring.academyPlatform.qa_comment.dto.QaCommentUpdateResponse;

@RestController
@RequestMapping("/api/v1/qa-comment")
@RequiredArgsConstructor
@Slf4j
public class QaCommentController {

	private final QaCommentService qaCommentService;

	@PostMapping("/comment")
	public ResponseEntity<QaCommentCreateResponse> createComment(@RequestBody QaCommentCreateRequest request,
		HttpSession session) {
		QaCommentCreateResponse result = qaCommentService.createComment(request, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PutMapping("/comment")
	public ResponseEntity<QaCommentUpdateResponse> changeComment(@RequestParam Long commentId,
		@RequestBody QaCommentUpdateRequest request,
		HttpSession session) {
		QaCommentUpdateResponse result = qaCommentService.updateComment(commentId, request, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@DeleteMapping("/comment")
	public ResponseEntity<QaCommentDeleteResponse> deleteComment(@RequestParam Long commentId) {
		QaCommentDeleteResponse result = qaCommentService.deleteComment(commentId);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
