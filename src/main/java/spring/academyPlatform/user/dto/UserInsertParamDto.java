package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInsertParamDto {
	@NotBlank
	String userId;
	@NotBlank
	String userPassword;
	@NotBlank
	String userName;
	@NotBlank
	String userType;

}
