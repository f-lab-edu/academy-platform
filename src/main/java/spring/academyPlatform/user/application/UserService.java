package spring.academyPlatform.user.application;

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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

	private final UserRepository userRepository;

	@Transactional
	public UserCreateResponse createUser(UserCreateRequest dto) {

		String encodedPassword = BcryptPasswordEncryptor.hashPassword(dto.getUserPassword());

		User user = UserMapper.fromDto(dto, encodedPassword);
		log.info("User created: {}", user.getDeletedYn().toString());
		User savedUser = userRepository.save(user);

		return UserMapper.fromEntity(savedUser);
	}

}
