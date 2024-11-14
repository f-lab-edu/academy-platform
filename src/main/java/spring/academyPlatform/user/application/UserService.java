package spring.academyPlatform.user.application;

import java.util.Optional;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.util.BcryptPasswordEncryptor;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

	private final UserRepository userRepository;

	private final BcryptPasswordEncryptor passwordEncryptor;

	public UserDto insertUser(UserDto dto) {

		String hashPassword = BcryptPasswordEncryptor.hashPassword(dto.getUserPassword());
		log.error("show encoding {} ", hashPassword);
		dto.setUserPassword(hashPassword);
		User result = userRepository.save(User.from(dto));

		return UserDto.from(result);
	}

	public UserDto findUser(Long id) {

		Optional<User> user = userRepository.findById(id);

		return null;
	}
}
