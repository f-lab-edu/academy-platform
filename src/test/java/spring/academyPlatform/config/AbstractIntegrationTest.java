package spring.academyPlatform.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Testcontainers
public class AbstractIntegrationTest {

	@Container
	private static final MariaDBContainer<?> mariaDBContainer = new MariaDBContainer<>("mariadb:10.6")
		.withDatabaseName("testdb")
		.withUsername("testuser")
		.withPassword("testpass");

	static {
		mariaDBContainer.start();
		System.setProperty("DB_URL", mariaDBContainer.getJdbcUrl());
		System.setProperty("DB_USERNAME", mariaDBContainer.getUsername());
		System.setProperty("DB_PASSWORD", mariaDBContainer.getPassword());
	}
}
