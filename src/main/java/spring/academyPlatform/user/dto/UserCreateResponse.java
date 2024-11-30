package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import spring.academyPlatform.user.model.TypeCode;
import spring.academyPlatform.user.model.YnCode;

@Getter
@Builder
public class UserCreateResponse {
	@NotBlank
	String userId;
	@NotBlank
	String userName;
	@NotBlank
	TypeCode userType;
	String createdBy;
	YnCode deletedYn;

	@Builder
	public UserCreateResponse(String userId, String userName,
		TypeCode userType, String createdBy, YnCode deletedYn) {
		this.userId = userId;
		this.userName = userName;
		this.userType = userType;
		this.createdBy = createdBy;
		this.deletedYn = deletedYn;
	}

}



