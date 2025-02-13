package spring.academyPlatform.user.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.user.model.UserTypeCode;

@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@ToString
@EntityListeners(AuditingEntityListener.class)
public class User {

	@Id
	@Column
	private String userId;

	@Column
	private String userPassword;

	@Column
	private String userName;

	@Enumerated(EnumType.STRING)
	@Column(name = "user_type")
	private UserTypeCode userType;

	@Column
	@CreatedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime createdAt;

	@Column
	@LastModifiedDate
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
	private LocalDateTime modifiedAt;

	@Column
	private String createdBy;

	@Column
	private String modifiedBy;

	@Column
	@Enumerated(EnumType.STRING)
	private YnCode deletedYn;

	@Builder
	public User(String userId, String userPassword, String userName, UserTypeCode userType, LocalDateTime createdAt,
		LocalDateTime modifiedAt, String createdBy, String modifiedBy, YnCode deletedYn) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userType = userType;
		this.createdAt = createdAt;
		this.modifiedAt = modifiedAt;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.deletedYn = deletedYn;
	}

}
