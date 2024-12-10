package spring.academyPlatform.user.mapper;

import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.dto.UserCreateResponse;
import spring.academyPlatform.user.model.UserTypeCode;

public class UserMapper {

	public static User fromDto(UserCreateRequest dto, String encodingPassword, UserTypeCode userTypeCode) {
		return User.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
			.userPassword(encodingPassword)
			.userType(userTypeCode)
			.createdBy(dto.getUserName())
			.build();
	}

	public static UserCreateResponse fromEntity(User user) {
		return UserCreateResponse.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userType(user.getUserType())
			.createdBy(user.getCreatedBy())
			.deletedYn(user.getDeletedYn())
			.build();

	}

}
