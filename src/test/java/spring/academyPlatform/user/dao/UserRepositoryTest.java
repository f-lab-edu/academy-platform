package spring.academyPlatform.user.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.global.util.BcryptPasswordEncryptor;
import spring.academyPlatform.user.domain.User;

class UserRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("사용자명으로 검색하기")
	void testFindUser() {

		User user = userRepository.findByUserName("홍길동2");

		assertThat(user).isNotNull();
		assertThat(user.getUserName()).isEqualTo("홍길동2");
	}

	@Test
	@DisplayName("회원가입 진행하기")
	void testSaveUser() {

		// given
		String password = "123456";

		BcryptPasswordEncryptor.hashPassword("123456");

		User user = User.builder()
			.userId("test_id")
			.userPassword(password)
			.userName("test_name")
			.userType("student")
			.deletedYn("Y")
			.build();

		// when
		User savedUser = userRepository.save(user);

		// then
		assertThat(savedUser).isNotNull();
		assertThat(savedUser.getUserName()).isEqualTo("test_name");
		assertThat(savedUser.getUserPassword()).isEqualTo(password);
		assertThat(savedUser.getUserType()).isEqualTo("student");
		assertThat(savedUser.getDeletedYn()).isEqualTo("Y");

	}
}
