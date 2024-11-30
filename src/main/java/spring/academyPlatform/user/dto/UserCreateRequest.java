package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import spring.academyPlatform.user.model.TypeCode;

@Getter
public class UserCreateRequest {
	@NotBlank
	String userId;
	@NotBlank
	String userPassword;
	@NotBlank
	String userName;
	@NotNull
	TypeCode userType;

}
