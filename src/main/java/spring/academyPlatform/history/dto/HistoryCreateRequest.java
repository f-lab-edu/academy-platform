package spring.academyPlatform.history.dto;

import java.util.Map;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class HistoryCreateRequest {

	@NotNull
	private String tableName;
	@NotNull
	private String tableId;
	@NotNull
	private String operationType;
	@NotNull
	private Map<String, Object> entityData; // 엔티티 객체를 담을 컬럼 지정
	@NotNull
	private String createdBy;

	public HistoryCreateRequest(String tableName, String tableId, String operationType, Map<String, Object> entityData,
		String createdBy) {
		this.tableName = tableName;
		this.tableId = tableId;
		this.operationType = operationType;
		this.entityData = entityData;
		this.createdBy = createdBy;
	}

}
