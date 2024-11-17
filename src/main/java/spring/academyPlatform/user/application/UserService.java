package spring.academyPlatform.user.application;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.mapper.UserMapper;
import spring.academyPlatform.global.util.BcryptPasswordEncryptor;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserInsertParamDto;
import spring.academyPlatform.user.dto.UserInsertResponseDto;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserInsertResponseDto insertUser(UserInsertParamDto dto) {

		String hashPassword = BcryptPasswordEncryptor.hashPassword(dto.getUserPassword());

		User user = User.builder()
			.userId(dto.getUserId())
			.userType(dto.getUserType())
			.userName(dto.getUserName())
			.userPassword(hashPassword)
			.createdBy(dto.getUserName())
			.deletedYn("Y")
			.build();
		User savedUser = userRepository.save(user);

		return UserMapper.fromEntity(savedUser);
	}

/*	public UserDto findUser(Long id) {

		Optional<User> user = userRepository.findById(id);

		return null;
	}*/
}
