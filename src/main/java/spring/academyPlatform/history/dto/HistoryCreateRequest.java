package spring.academyPlatform.history.dto;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HistoryCreateRequest {

	String tableName;
	String tableId;
	String operationType;
	String changedData;
	String createdBy;

	public HistoryCreateRequest(String tableName, String tableId, String operationType, String changedData,
		String createdBy) {
		this.tableName = tableName;
		this.tableId = tableId;
		this.operationType = operationType;
		this.changedData = changedData;
		this.createdBy = createdBy;
	}

}
