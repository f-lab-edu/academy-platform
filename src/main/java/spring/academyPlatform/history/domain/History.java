package spring.academyPlatform.history.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import spring.academyPlatform.global.model.YnCode;

@Entity
@Table(name = "history")
@EntityListeners(AuditingEntityListener.class)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
public class History {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "history_id")
	private Long id;

	@Column(name = "table_name")
	private String tableName;

	@Column(name = "table_id")
	private String tableId;

	@Column(name = "operation_type")
	private String operationType;

	@Column(name = "changed_data")
	private String changedData;

	@Column
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	@Column(name = "created_by")
	private String createdBy;

	@Column(name = "deleted_yn")
	@Enumerated(EnumType.STRING)
	private YnCode deletedYn;

	@Builder
	public History(Long id, String tableName, String tableId,
		String operationType, String changedData, String createdBy,
		YnCode deletedYn) {
		this.id = id;
		this.tableName = tableName;
		this.tableId = tableId;
		this.operationType = operationType;
		this.changedData = changedData;
		this.createdBy = createdBy;
		this.deletedYn = deletedYn != null ? deletedYn : YnCode.N;
	}
}
