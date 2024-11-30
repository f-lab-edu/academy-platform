package spring.academyPlatform.user.mapper;

import org.springframework.stereotype.Component;

import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.dto.UserCreateResponse;

@Component
public class UserMapper {

	public static User fromDto(UserCreateRequest dto, String encodingPassword) {
		return User.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
			.userPassword(encodingPassword)
			.userType(dto.getUserType())
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
