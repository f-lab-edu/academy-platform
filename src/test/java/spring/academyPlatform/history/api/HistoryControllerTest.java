package spring.academyPlatform.history.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.history.dao.HistoryRepository;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryCreateRequest;
import spring.academyPlatform.history.dto.HistorySearchRequest;
import spring.academyPlatform.history.mapper.HistoryMapper;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // rest docs 자동 설정
@ActiveProfiles("test") // 'test' 프로파일 활성화
@Slf4j
class HistoryControllerTest extends AbstractIntegrationTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private HistoryRepository historyRepository;

	@Test
	@DisplayName("히스토리 조회 테스트")
	@Transactional
	void find_history() throws Exception {

		User user = userRepository.save(User.builder()
			.userId("testId")
			.userType("student")
			.userName("test")
			.userPassword("1234")
			.createdBy("test")
			.deletedYn("Y")
			.build());

		HistoryCreateRequest dto2 = new HistoryCreateRequest("테스트 테이블", "테스트 테이블 id", "생성", user.toString(), "테스트 유저");

		History history = HistoryMapper.from(dto2);
		historyRepository.save(history);

		HistorySearchRequest dto = HistorySearchRequest.builder()
			.tableName("테스트 테이블")
			.operationType("생성")
			.startDate("20240101")
			.endDate("20241231")
			.build();

		mockMvc.perform(get("/api/v1/history/histories")
				.param("tableName", dto.getTableName())
				.param("operationType", dto.getOperationType())
				.param("startDate", dto.getStartDate())
				.param("endDate", dto.getEndDate())
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()))
			.andExpect(status().isOk())
			.andDo(document("histories", // 문서 조각 디렉토리 명
				queryParameters( // 요청 본문 필드 정보 입력
					parameterWithName("tableName").description("테이블 이름"),
					parameterWithName("operationType").description("작업형식"),
					parameterWithName("startDate").description("시작 날짜"),
					parameterWithName("endDate").description("종료 날짜")
				),
				responseFields( // 응답 필드 정보 입력
					fieldWithPath("[].id").description("아이디"),
					fieldWithPath("[].tableName").description("테이블명"),
					fieldWithPath("[].tableId").description("테이블 아이디"),
					fieldWithPath("[].operationType").description("작업형식"),
					fieldWithPath("[].changedData").description("변경정보"),
					fieldWithPath("[].createdAt").description("생성일시"),
					fieldWithPath("[].createdBy").description("생성자"),
					fieldWithPath("[].deletedYn").description("삭제여부")
				)));
	}

}
