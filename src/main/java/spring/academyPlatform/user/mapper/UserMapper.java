package spring.academyPlatform.user.mapper;

import org.springframework.stereotype.Component;

import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserInsertParamDto;
import spring.academyPlatform.user.dto.UserInsertResponseDto;

/**
 * @Component
 * 스프링에서 빈으로 등록할 클래스를 명시하는 어노테이션 입니다.
 */
@Component
public class UserMapper {

	public static User from(UserInsertResponseDto dto) {
		return User.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
			.userType(dto.getUserType())
			.createdBy(dto.getUserName())
			.build();
	}

	public static UserInsertResponseDto fromEntity(User user) {
		return UserInsertResponseDto.builder()
			.userId(user.getUserId())
			.userName(user.getUserName())
			.userType(user.getUserType())
			.createdBy(user.getCreatedBy())
			.deletedYn(user.getDeletedYn())
			.build();

	}

	public static UserInsertResponseDto fromParamDto(UserInsertParamDto dto) {
		return UserInsertResponseDto.builder()
			.userId(dto.getUserId())
			.userName(dto.getUserName())
			.userType(dto.getUserType())
			.build();
	}

}
