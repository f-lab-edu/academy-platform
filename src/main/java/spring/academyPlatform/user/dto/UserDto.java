package spring.academyPlatform.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import spring.academyPlatform.user.domain.User;

@Data
public class UserDto {
	@NotBlank
	String userId;
	@NotBlank
	String userPassword;
	@NotBlank
	String userName;
	@NotBlank
	String userType;
	@NotBlank
	String createdBy;
	String modifiedBy;
	@NotBlank
	String deletedYn;

	@Builder
	public UserDto(String userId, String userPassword, String userName, String userType, String createdBy,
		String modifiedBy, String deletedYn) {
		this.userId = userId;
		this.userPassword = userPassword;
		this.userName = userName;
		this.userType = userType;
		this.createdBy = createdBy;
		this.modifiedBy = modifiedBy;
		this.deletedYn = deletedYn;
	}

	public static UserDto from(User user) {
		return UserDto.builder()
			.userId(user.getUserId())
			.userPassword(user.getUserPassword())
			.userName(user.getUserName())
			.userType(user.getUserType())
			.createdBy(user.getCreatedBy())
			.modifiedBy(user.getModifiedBy())
			.deletedYn(user.getDeletedYn())
			.build();

	}

}
