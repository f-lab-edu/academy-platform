package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.user.model.UserTypeCode;

@Getter
@Builder
public class UserCreateResponse {
	@NotBlank
	private String userId;
	@NotBlank
	private String userName;
	@NotBlank
	private UserTypeCode userType;
	private String createdBy;
	private YnCode deletedYn;

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



