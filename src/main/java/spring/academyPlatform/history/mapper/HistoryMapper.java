package spring.academyPlatform.history.mapper;

import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryCreateRequest;
import spring.academyPlatform.history.dto.HistoryPatchResponse;
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

	public static History from(HistoryPatchResponse dto) {
		return History.builder()
			.id(dto.getId())
			.tableName(dto.getTableName())
			.tableId(dto.getTableId())
			.changedData(dto.getChangedData())
			.operationType(dto.getOperationType())
			.createdBy(dto.getCreatedBy())
			.deletedYn(YnCode.valueOf(dto.getDeletedYn()))
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

	public static HistoryPatchResponse fromPatchResponse(History entity) {
		return HistoryPatchResponse.builder()
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
