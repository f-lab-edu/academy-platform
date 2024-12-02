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
