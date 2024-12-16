package spring.academyPlatform.history.application;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.util.DateTimeFormatterUtil;
import spring.academyPlatform.history.dao.HistoryRepository;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryCreateRequest;
import spring.academyPlatform.history.dto.HistoryCreateResponse;
import spring.academyPlatform.history.dto.HistorySearchResponse;
import spring.academyPlatform.history.mapper.HistoryMapper;

@Service
@RequiredArgsConstructor
@Slf4j
public class HistoryService {

	private final HistoryRepository historyRepository;
	private final ObjectMapper objectMapper;

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

	@Transactional
	public HistoryCreateResponse createHistory(HistoryCreateRequest historyCreateRequest) {
		Map<String, Object> changedMap = historyCreateRequest.getEntityData(); // json 데이터를 String 형식으로 직렬화
		String changedDataJson;
		try {
			changedDataJson = objectMapper.writeValueAsString(changedMap);
		} catch (JsonProcessingException e) {
			log.error("serialize error ", e);
			throw new RuntimeException("Failed to serialize changed data", e);
		}

		History result = historyRepository.save(HistoryMapper.from(historyCreateRequest, changedDataJson));
		return HistoryMapper.fromCreateResponse(result);
	}

}
