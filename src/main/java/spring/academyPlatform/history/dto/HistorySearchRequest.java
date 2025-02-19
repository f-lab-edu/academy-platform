package spring.academyPlatform.history.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

@Getter
public class HistorySearchRequest {

	String tableName;
	String operationType;
	@Pattern(regexp = "\\d{8}", message = "startDate must be in yyyyMMdd format")
	String startDate;
	@Pattern(regexp = "\\d{8}", message = "endDate must be in yyyyMMdd format")
	String endDate;
	String createdBy;

	@Builder
	public HistorySearchRequest(String tableName, String operationType, String startDate,
		String endDate, String createdBy) {
		this.tableName = tableName;
		this.operationType = operationType;
		this.startDate = startDate;
		this.endDate = endDate;
		this.createdBy = createdBy;
	}

}
