package spring.academyPlatform.history.dao;

import java.time.LocalDateTime;
import java.util.List;

import spring.academyPlatform.history.domain.History;

public interface HistoryCustomRepository {
	List<History> readHistory(String tableName, String operationType, LocalDateTime startDate,
		LocalDateTime endDate, String createBy);
}
