package spring.academyPlatform.qa_board.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.qa_board.application.QaBoardService;
import spring.academyPlatform.qa_board.dto.QaBoardCreateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardCreateResponse;

@RestController
@RequestMapping("/api/v1/qa-board")
@RequiredArgsConstructor
@Slf4j
public class QaBoardController {

	private final QaBoardService qaBoardService;

	@PostMapping("/board")
	public ResponseEntity<?> createBoard(@RequestBody QaBoardCreateRequest request, HttpSession session) {
		QaBoardCreateResponse result = qaBoardService.insertBoard(request, session);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}
