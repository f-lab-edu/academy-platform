package spring.academyPlatform.global.config.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession // 스프링 애플리케이션이 Redis 가 지원하는 분산 세션 (distributed sessions backed by Redis) 을 사용할 수 있게 해준다.
public class RedisConfig {

	@Value("${spring.data.redis.host}")
	private String host;

	@Value("${spring.data.redis.password}")
	private String password;

	@Value("${spring.data.redis.port}")
	private int port;

	/**
	 *
	 * lettuceConnectionFactory() 메서드는 Spring Boot 애플리케이션에서 Redis 서버와 통신하기 위해 Lettuce 클라이언트를 기반으로 한 ConnectionFactory를 생성하는 역할을 합니다.
	 * Spring Data Redis 모듈 내에서 Redis와의 실제 연결(connection)을 맺어주는 핵심 컴포넌트이며, 이 Bean을 통해 RedisTemplate, Redis Repositories, Spring Session 등에서 Redis에 접근할 수 있게 됩니다
	 * 스프링에서 제공하는 다양한 Redis 연동 기능(예: RedisTemplate, SessionRepository 등)이 내부적으로 이 Connection Factory를 참조하여 Redis에 접근하고, 명령을 수행하고, 결과를 가져옵니다.
	 */
	@Bean
	public LettuceConnectionFactory lettuceConnectionFactory() {
		final RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);
		redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
		return new LettuceConnectionFactory(redisStandaloneConfiguration);
	}

	// redis에 데이터를 직렬화하여 저장하기 위한 설정입니다.
	@Bean
	public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
		RedisTemplate<String, Object> template = new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);

		// key는 StringRedisSerializer 사용
		template.setKeySerializer(new StringRedisSerializer());
		template.setHashKeySerializer(new StringRedisSerializer());

		// value는 JSON Serializer 사용
		template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
		template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());

		template.afterPropertiesSet();
		return template;
	}

	// Spring Session이 사용할 기본 RedisSerializer 설정
	@Bean(name = "springSessionDefaultRedisSerializer")
	public RedisSerializer<Object> springSessionDefaultRedisSerializer() {
		return new GenericJackson2JsonRedisSerializer();
	}

}
