package spring.academyPlatform.user.application;

import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.global.util.BcryptPasswordEncryptor;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.dto.UserCreateResponse;
import spring.academyPlatform.user.mapper.UserMapper;

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
@Transactional
public class UserService {

	private final UserRepository userRepository;

	public UserCreateResponse insertUser(UserCreateRequest dto) {

		String hashPassword = BcryptPasswordEncryptor.hashPassword(dto.getUserPassword());

		// 사용자 중복 확인
		if (userRepository.findById(dto.getUserId()).isPresent()) {
			throw new IllegalArgumentException("User ID already exists");
		}

		User user = User.builder()
			.userId(dto.getUserId())
			.userType(dto.getUserType())
			.userName(dto.getUserName())
			.userPassword(hashPassword)
			.createdBy(dto.getUserName())
			.deletedYn(YnCode.Y)
			.build();
		User savedUser = userRepository.save(user);

		return UserMapper.fromEntity(savedUser);
	}

	public boolean authenticate(String userId, String password, HttpSession session) {

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new IllegalArgumentException("해당 아이디는 존재하지 않습니다."));

		if (user.getUserId().equals(userId) && BcryptPasswordEncryptor.checkPassword(password,
			user.getUserPassword())) {
			// 로그인 시 세션에 유저 정보를 저장합니다.
			session.setAttribute("user", user.getUserName());
			return true;
		} else {
			throw new IllegalArgumentException("입력한 정보가 올바르지 않습니다");
		}
	}
}
