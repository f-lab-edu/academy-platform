package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.user.model.UserTypeCode;

@Getter
public class UserCreateRequest {

	@NotBlank
	String userId;
	@NotBlank
	String userPassword;
	@NotBlank
	String userName;
	@NotBlank
	UserTypeCode userType;

	@Builder
	public UserCreateRequest(String userId, String userPassword, String userName, UserTypeCode userType) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userType = userType;
	}
}
