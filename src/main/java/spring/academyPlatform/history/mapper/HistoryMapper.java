package spring.academyPlatform.history.mapper;

import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryCreateRequest;
import spring.academyPlatform.history.dto.HistorySearchResponse;

public class HistoryMapper {

	public static History from(HistoryCreateRequest dto) {
		return History.builder()
			.tableName(dto.getTableName())
			.tableId(dto.getTableId())
			.changedData(dto.getChangedData())
			.operationType(dto.getOperationType())
			.createdBy(dto.getCreatedBy())
			.build();
	}

	public static HistorySearchResponse fromEntity(History entity) {
		return HistorySearchResponse.builder()
			.id(entity.getId())
			.tableName(entity.getTableName())
			.tableId(entity.getTableId())
			.operationType(entity.getOperationType())
			.createdBy(entity.getCreatedBy())
			.changedData(entity.getChangedData())
			.deletedYn(String.valueOf(entity.getDeletedYn()))
			.build();
	}

}
