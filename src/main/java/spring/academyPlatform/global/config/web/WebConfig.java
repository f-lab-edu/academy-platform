package spring.academyPlatform.global.config.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import spring.academyPlatform.global.interceptor.AuthenticationInterceptor;

@Configuration
@Profile("!test")
public class WebConfig implements WebMvcConfigurer {
	private final AuthenticationInterceptor authenticationInterceptor;

	@Autowired
	public WebConfig(AuthenticationInterceptor authenticationInterceptor) {
		this.authenticationInterceptor = authenticationInterceptor;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(authenticationInterceptor)
			.addPathPatterns("/**") // 모든 요청에 적용
			.excludePathPatterns("/api/v1/user/**", "/favicon.ico", "/static/**", "/resources/**"); // 로그인 관련 경로 제외
	}
}
