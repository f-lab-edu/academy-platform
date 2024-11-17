package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
public class UserInsertResponseDto {
	@NotBlank
	String userId;
	@NotBlank
	String userName;
	@NotBlank
	String userType;
	String createdBy;
	@NotBlank
	String deletedYn;

	@Builder
	public UserInsertResponseDto(String userId, String userName,
		String userType, String createdBy, String deletedYn) {
		this.userId = userId;
		this.userName = userName;
		this.userType = userType;
		this.createdBy = createdBy;
		this.deletedYn = deletedYn;
	}

}
