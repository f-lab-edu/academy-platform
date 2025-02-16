package spring.academyPlatform.qa_board.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.util.CustomPage;
import spring.academyPlatform.qa_board.application.QaBoardService;
import spring.academyPlatform.qa_board.dto.QaBoardChangeResponse;
import spring.academyPlatform.qa_board.dto.QaBoardCreateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardCreateResponse;
import spring.academyPlatform.qa_board.dto.QaBoardSearchResponse;
import spring.academyPlatform.qa_board.dto.QaBoardUpdateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardUpdateResponse;

@RestController
@RequestMapping("/api/v1/qa-board")
@RequiredArgsConstructor
@Slf4j
public class QaBoardController {

	private final QaBoardService qaBoardService;

	@PostMapping("/board")
	public ResponseEntity<QaBoardCreateResponse> createBoard(@RequestBody QaBoardCreateRequest request,
		HttpSession session) {
		QaBoardCreateResponse result = qaBoardService.insertBoard(request, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping("/boards")
	public ResponseEntity<CustomPage<QaBoardChangeResponse>> searchBoard(@RequestParam(required = false) Long boardId,
		@RequestParam(required = false) String title,
		@RequestParam(required = false) String userId,
		@RequestParam(required = false) @Pattern(regexp = "\\d{8}", message = "시작일은 다음과 같은 형식입니다. yyyyMMdd format") String startDate,
		@RequestParam(required = false) @Pattern(regexp = "\\d{8}", message = "종료일은 다음과 같은 형식입니다. yyyyMMdd format") String endDate,
		@RequestParam(defaultValue = "1") int page,
		@RequestParam(defaultValue = "10") int size) {
		CustomPage<QaBoardChangeResponse> result = qaBoardService.findBoard(boardId, title, userId, startDate, endDate,
			page, size);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@GetMapping("/board")
	public ResponseEntity<QaBoardSearchResponse> searchBoard(@RequestParam(required = false) Long boardId) {
		QaBoardSearchResponse result = qaBoardService.findSingleBoard(boardId);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PutMapping("/board")
	public ResponseEntity<QaBoardUpdateResponse> changeBoard(@RequestParam(required = false) Long boardId,
		@RequestBody QaBoardUpdateRequest dto, HttpSession session) {
		QaBoardUpdateResponse result = qaBoardService.changeBoard(boardId, dto, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@DeleteMapping("/board")
	public ResponseEntity<Boolean> deleteBoard(@RequestParam(required = false) Long boardId) {
		boolean result = qaBoardService.deletedBoard(boardId);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
