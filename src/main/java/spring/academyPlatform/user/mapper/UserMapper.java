package spring.academyPlatform.user.mapper;

import org.springframework.stereotype.Component;

import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.dto.UserCreateResponse;

/**
 * @Component
 * 스프링에서 빈으로 등록할 클래스를 명시하는 어노테이션 입니다.
 */
@Component
public class UserMapper {

	public static User from(UserCreateResponse dto) {
		return User.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
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

	public static UserCreateResponse fromParamDto(UserCreateRequest dto) {
		return UserCreateResponse.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
			.userType(dto.getUserType())
			.build();
	}

}
