package spring.academyPlatform.qa_comment.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import spring.academyPlatform.qa_comment.dto.QaCommentResponse;
import spring.academyPlatform.qa_comment.dto.QaCommentUpdateRequest;

@RestController
@RequestMapping("/api/v1/qa-comment")
@RequiredArgsConstructor
@Slf4j
public class QaCommentController {

	private final QaCommentService qaCommentService;

	@PostMapping("/comment")
	public ResponseEntity<QaCommentResponse> createBoard(@RequestBody QaCommentCreateRequest request,
		HttpSession session) {
		QaCommentResponse result = qaCommentService.createComment(request, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PutMapping("/comment")
	public ResponseEntity<QaCommentResponse> changeBoard(@RequestParam Long commentId,
		@RequestBody QaCommentUpdateRequest request,
		HttpSession session) {
		QaCommentResponse result = qaCommentService.updateComment(commentId, request, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}
}
