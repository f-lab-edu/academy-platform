package spring.academyPlatform;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

import lombok.Generated;

@EnableJpaAuditing
@SpringBootApplication
@Generated
@EnableRedisHttpSession // redis에 세션을 저장하기 위한 설정
public class AcademyPlatformApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyPlatformApplication.class, args);
	}

}
