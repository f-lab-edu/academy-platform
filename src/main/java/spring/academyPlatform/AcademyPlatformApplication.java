package spring.academyPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import lombok.Generated;

@EnableJpaAuditing
@SpringBootApplication
@Generated // jacoco에서 해당 어노테이션이 붙은 클래스의 테스트 커버리지를 측정하지 않도록 합니다.
public class AcademyPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyPlatformApplication.class, args);
	}

}
