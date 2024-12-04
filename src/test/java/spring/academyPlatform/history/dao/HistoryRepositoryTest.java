package spring.academyPlatform.history.dao;

import static org.assertj.core.api.AssertionsForInterfaceTypes.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.global.config.querydsl.QueryDslConfig;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryCreateRequest;
import spring.academyPlatform.history.mapper.HistoryMapper;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;

/**
 * @DataJPATest JPA 관련 빈만 로드함.
 * @Import(QueryDslConfig.class) QueryDsl 설정 추가
 * @AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) @DataJpaTest가 테스트컨테이너를 사용하도록함
 */
@DataJpaTest
@Import(QueryDslConfig.class) // QueryDsl 설정 추가
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // @DataJpaTest가 테스트컨테이너를 사용하도록함
@ActiveProfiles("test") // 'test' 프로파일 활성화
@Slf4j
class HistoryRepositoryTest extends AbstractIntegrationTest {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HistoryRepository historyRepository;

	@BeforeEach
	void setUp() {
		// 모든 유저 데이터 삭제
		userRepository.deleteAll();

	}

	@Test
	@DisplayName("히스토리 생성")
	void create_history() {
		User user = userRepository.save(User.builder()
			.userId("testId")
			.userType("student")
			.userName("test")
			.userPassword("1234")
			.createdBy("test")
			.deletedYn("Y")
			.build());
		// when
		HistoryCreateRequest dto2 = new HistoryCreateRequest("테스트 테이블", "테스트 테이블 id", "생성", user.toString(), "테스트 유저");
		History history = HistoryMapper.from(dto2);
		historyRepository.save(history);
		// then
		assertThat(dto2.getCreatedBy()).isEqualTo(history.getCreatedBy());
		assertThat(dto2.getTableName()).isEqualTo(history.getTableName());
	}
	
}
