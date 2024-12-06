package spring.academyPlatform.history.api;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.history.application.HistoryService;
import spring.academyPlatform.history.dto.HistorySearchResponse;

@RestController
@RequestMapping("api/v1/history")
@RequiredArgsConstructor
@Slf4j
public class HistoryController {

	private final HistoryService historyService;

	@GetMapping("/histories")
	public ResponseEntity<List<HistorySearchResponse>> getHistory(
		@RequestParam(required = false) String tableName,
		@RequestParam(required = false) String operationType,
		@RequestParam @Pattern(regexp = "\\d{8}", message = "시작일은 다음과 같은 형식입니다. yyyyMMdd format") String startDate,
		@RequestParam @Pattern(regexp = "\\d{8}", message = "종료일은 다음과 같은 형식입니다. yyyyMMdd format") String endDate,
		@RequestParam(required = false) String createBy) {

		List<HistorySearchResponse> result = historyService.searchHistory(tableName, operationType, startDate,
			endDate, createBy);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

	@PutMapping("/{historyId}")
	public ResponseEntity<Boolean> deleteHistory(@PathVariable Long historyId) {
		boolean result = historyService.deleteHistory(historyId);
		return ResponseEntity.status(HttpStatus.OK).body(result);
	}

}

