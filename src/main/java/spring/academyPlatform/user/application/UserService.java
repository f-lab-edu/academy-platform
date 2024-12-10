package spring.academyPlatform.user.application;

import java.util.Arrays;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.util.BcryptPasswordEncryptor;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.dto.UserCreateResponse;
import spring.academyPlatform.user.mapper.UserMapper;
import spring.academyPlatform.user.model.UserTypeCode;

/**
 * @Service
 * @Component를 포함하고 있습니다.
 * 해당 클래스가 서비스의 역할을 한다고 명확하게 표시가힏 위해서 사용합니다.
 * @Transactional
 * 영속성 컨텍스트의 생명주기를 관리합니다.
 * 메서드나 클래스에 트랜잭션 범위를 설정하는 데 사용됩니다.
 * 데이터베이스 작업이 원자성을 가지도록 보장하며, 예외 발생 시 작업을 롤백합니다.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserCreateResponse createUser(UserCreateRequest dto) {

		UserTypeCode userTypeCode = convertToEnum(dto.getUserType());
		String encodedPassword = BcryptPasswordEncryptor.hashPassword(dto.getUserPassword());
		User user = UserMapper.fromDto(dto, encodedPassword, userTypeCode);
		log.info("User created: {}", user.toString());
		User savedUser = userRepository.save(user);

		return UserMapper.fromEntity(savedUser);
	}

	public UserTypeCode convertToEnum(String userType) {
		return Arrays.stream(UserTypeCode.values())
			.filter(e -> e.name().equalsIgnoreCase(userType))
			.findFirst()
			.orElseThrow(() -> new IllegalArgumentException("잘못된 유저 타입입니다 : " + userType));
	}

}
