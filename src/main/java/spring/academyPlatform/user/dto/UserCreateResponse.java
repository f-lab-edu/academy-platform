package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.user.model.UserTypeCode;

@Getter
public class UserCreateResponse {
	@NotBlank
	String userId;
	@NotBlank
	String userName;
	@NotBlank
	UserTypeCode userType;
	String createdBy;
	@NotBlank
	YnCode deletedYn;

	@Builder
	public UserCreateResponse(String userId, String userName,
		UserTypeCode userType, String createdBy, YnCode deletedYn) {
		this.userId = userId;
		this.userName = userName;
		this.userType = userType;
		this.createdBy = createdBy;
		this.deletedYn = deletedYn;
	}

}
