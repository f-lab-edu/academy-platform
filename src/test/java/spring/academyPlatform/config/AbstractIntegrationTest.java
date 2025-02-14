package spring.academyPlatform.config;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.redis.testcontainers.RedisContainer;

import lombok.extern.slf4j.Slf4j;

@Testcontainers // Testcontainers 라이브러리를 사용하여 테스트 컨테이너를 초기화하고 관리하기 위한 어노테이션입니다.
@Slf4j
public abstract class AbstractIntegrationTest { // 통합 테스트에서 공통으로 사용할 추상 클래스입니다.

	@Container // Testcontainers에서 해당 컨테이너를 테스트 수명 주기에 따라 자동으로 관리하도록 지정합니다.
	private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>(
		"mariadb:10.6") // MariaDB 버전 10.6의 테스트 컨테이너를 생성합니다.
		.withDatabaseName("testdb") // 컨테이너 내부의 데이터베이스 이름을 'testdb'로 설정합니다.
		.withUsername("testuser") // 데이터베이스 접속에 사용할 사용자명을 'testuser'로 설정합니다.
		.withPassword("testpass"); // 데이터베이스 접속에 사용할 비밀번호를 'testpass'로 설정합니다.

	@Container
	public static RedisContainer redisContainer = new RedisContainer("latest");

	@DynamicPropertySource // spring boot가 테스트 중에 사용할 데이터소스 속성을 동적으로 설정합니다.
	static void configureProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", mariaDBContainer::getJdbcUrl);
		registry.add("spring.datasource.username", mariaDBContainer::getUsername);
		registry.add("spring.datasource.password", mariaDBContainer::getPassword);
		registry.add("spring.jpa.properties.hibernate.dialect", () -> "org.hibernate.dialect.MariaDBDialect");

		registry.add("spring.data.redis.host", redisContainer::getHost);
		registry.add("spring.data.redis.port", () -> redisContainer.getMappedPort(6379));
	}
}
