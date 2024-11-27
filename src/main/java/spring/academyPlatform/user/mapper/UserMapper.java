package spring.academyPlatform.user.mapper;

import org.springframework.stereotype.Component;

import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserCreateRequestDto;
import spring.academyPlatform.user.dto.UserCreateResponseDto;

@Component
public class UserMapper {

	public static User fromDto(UserCreateRequestDto dto, String encodingPassword) {
		return User.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
			.userPassword(encodingPassword)
			.userType(dto.getUserType())
			.createdBy(dto.getUserName())
			.build();
	}

	public static UserCreateResponseDto fromEntity(User user) {
		return UserCreateResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userType(user.getUserType())
			.createdBy(user.getCreatedBy())
			.deletedYn(user.getDeletedYn())
			.build();

	}

}
