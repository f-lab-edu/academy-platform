package spring.academyPlatform.user.dao;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.user.domain.User;

class UserRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	@DisplayName("사용자명으로 검색하기")
	void testFindUser() {
		// Arrange
		User user = userRepository.findByUserName("홍길동2");

		// Assert
		assertThat(user).isNotNull();
		assertThat(user.getUserName()).isEqualTo("홍길동2");
	}
}
