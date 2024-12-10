package spring.academyPlatform.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest // 스프링 부트 애플리케이션의 통합 테스트를 수행하기 위한 어노테이션으로, 애플리케이션 컨텍스트를 로드합니다.
@Testcontainers // Testcontainers 라이브러리를 사용하여 테스트 컨테이너를 초기화하고 관리하기 위한 어노테이션입니다.
public abstract class AbstractIntegrationTest { // 통합 테스트에서 공통으로 사용할 추상 클래스입니다.

	@Container // Testcontainers에서 해당 컨테이너를 테스트 수명 주기에 따라 자동으로 관리하도록 지정합니다.
	private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>(
		"mariadb:10.6") // MariaDB 버전 10.6의 테스트 컨테이너를 생성합니다.
		.withDatabaseName("testdb") // 컨테이너 내부의 데이터베이스 이름을 'testdb'로 설정합니다.
		.withUsername("testuser") // 데이터베이스 접속에 사용할 사용자명을 'testuser'로 설정합니다.
		.withPassword("testpass"); // 데이터베이스 접속에 사용할 비밀번호를 'testpass'로 설정합니다.

	static { // 정적 초기화 블록으로, 클래스 로딩 시 한 번 실행됩니다.
		mariaDBContainer.start(); // MariaDB 컨테이너를 시작합니다.
		System.setProperty("DB_URL", mariaDBContainer.getJdbcUrl()); // 시스템 프로퍼티에 'DB_URL' 키로 컨테이너의 JDBC URL을 설정합니다.
		System.setProperty("DB_USERNAME",
			mariaDBContainer.getUsername()); // 시스템 프로퍼티에 'DB_USERNAME' 키로 컨테이너의 사용자명을 설정합니다.
		System.setProperty("DB_PASSWORD",
			mariaDBContainer.getPassword()); // 시스템 프로퍼티에 'DB_PASSWORD' 키로 컨테이너의 비밀번호를 설정합니다.
	}
}
