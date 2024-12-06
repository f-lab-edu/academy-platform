package spring.academyPlatform.history.application;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.global.util.DateTimeFormatterUtil;
import spring.academyPlatform.history.dao.HistoryRepository;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryPatchResponse;
import spring.academyPlatform.history.dto.HistorySearchResponse;
import spring.academyPlatform.history.mapper.HistoryMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

	private final HistoryRepository historyRepository;

	@Transactional(readOnly = true)
	public List<HistorySearchResponse> searchHistory(String tableName, String operationType, String startDate,
		String endDate, String createBy) {

		LocalDateTime start = DateTimeFormatterUtil.parse(startDate).atStartOfDay(); // 날짜 범위 시작일
		LocalDateTime end = DateTimeFormatterUtil.parse(endDate).atTime(LocalTime.MAX); // 날짜 범위 종료일

		List<History> result = historyRepository.readHistory(tableName, operationType,
			start, end, createBy);

		return result.stream()
			.map(HistoryMapper::fromEntity)
			.toList();
	}

	public boolean deleteHistory(Long historyId) {
		History history = historyRepository.findById(historyId)
			.orElseThrow(() -> new IllegalArgumentException("history is not exist"));

		if (history.getDeletedYn().equals(YnCode.Y)) {
			throw new IllegalArgumentException("history is deleted");
		}

		HistoryPatchResponse response = HistoryMapper.fromPatchResponse(history.builder()
			.id(historyId)
			.tableName(history.getTableName())
			.createdBy(history.getCreatedBy())
			.changedData(history.getChangedData())
			.tableId(history.getTableId())
			.operationType(history.getOperationType())
			.deletedYn(YnCode.Y)
			.build()
		);
		historyRepository.save(HistoryMapper.from(response));
		return true;
	}
}
