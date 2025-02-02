package spring.academyPlatform.history.dto;

import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Getter;

@Getter
public class HistorySearchResponse {
	Long id;
	String tableName;
	String tableId;
	String operationType;
	String changedData;
	LocalDateTime createdAt;
	String createdBy;
	String deletedYn;

	@Builder
	public HistorySearchResponse(Long id, String tableName, String tableId, String operationType, String changedData,
		LocalDateTime createdAt, String createdBy, String deletedYn) {
		this.id = id;
		this.tableName = tableName;
		this.tableId = tableId;
		this.operationType = operationType;
		this.changedData = changedData;
		this.createdAt = createdAt;
		this.createdBy = createdBy;
		this.deletedYn = deletedYn;
	}
}
