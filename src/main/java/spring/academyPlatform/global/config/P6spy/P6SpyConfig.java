package spring.academyPlatform.global.config.P6spy;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Configuration
 *  자바 클래스가 하나 이상의 빈(Bean) 정의를 제공하고 이 빈들을 스프링 컨테이너에 등록하는 데 사용된다는 것을 나타냅니다.
 */

@Configuration
public class P6SpyConfig {
	@Bean
	public P6SpyEventListener p6SpyCustomEventListener() {
		return new P6SpyEventListener();
	}

	@Bean
	public P6SpyFormatter p6SpyCustomFormatter() {
		return new P6SpyFormatter();
	}
}
