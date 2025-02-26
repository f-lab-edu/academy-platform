package spring.academyPlatform.history.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;

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

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.history.dao.HistoryRepository;
import spring.academyPlatform.history.domain.History;
import spring.academyPlatform.history.dto.HistoryCreateRequest;
import spring.academyPlatform.history.dto.HistorySearchRequest;
import spring.academyPlatform.history.mapper.HistoryMapper;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.model.UserTypeCode;

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
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("히스토리 조회 테스트")
	@Transactional
	void find_history_success() throws Exception {

		User user = userRepository.save(User.builder()
			.userId("testId")
			.userType(UserTypeCode.STUDENT)
			.userName("test")
			.userPassword("1234")
			.createdBy("test")
			.deletedYn(YnCode.Y)
			.build());

		Map<String, Object> changedData = objectMapper.convertValue(user, Map.class);

		HistoryCreateRequest dto2 = new HistoryCreateRequest("테스트 테이블", "테스트 테이블 id", "생성", changedData, "테스트 유저");

		String resultData = objectMapper.writeValueAsString(changedData);

		History history = HistoryMapper.from(dto2, resultData);
		historyRepository.save(history);

		HistorySearchRequest dto = HistorySearchRequest.builder()
			.tableName("테스트 테이블")
			.operationType("생성")
			.startDate("20240101")
			.endDate("20251231")
			.build();

		mockMvc.perform(get("/api/v1/histories/history")
				.param("tableName", dto.getTableName())
				.param("operationType", dto.getOperationType())
				.param("startDate", dto.getStartDate())
				.param("endDate", dto.getEndDate())
				.contentType(MediaType.APPLICATION_JSON))
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

	@Test
	@DisplayName("히스토리 생성 성공 테스트")
	@Transactional
	void create_history_success() throws Exception {

		User user = userRepository.save(User.builder()
			.userId("testId")
			.userType(UserTypeCode.STUDENT)
			.userName("test")
			.userPassword("1234")
			.createdBy("test")
			.deletedYn(YnCode.Y)
			.build());

		// User 객체를 String으로 변경
		Map<String, Object> changedData = objectMapper.convertValue(user, Map.class);

		log.info("changedData: {}", changedData);
		HistoryCreateRequest dto = new HistoryCreateRequest(
			"테스트 테이블",
			"테스트 테이블 id",
			"생성",
			changedData,
			"테스트 유저");

		mockMvc.perform(post("/api/v1/histories/history")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andDo(result -> log.info("Response: {}", result.getResponse().getContentAsString()))
			.andExpect(status().isOk())
			.andDo(document("create_history", // 문서 조각 디렉토리 명
				requestFields( // 요청 본문 필드 정보 입력
					fieldWithPath("tableName").description("테이블명"),
					fieldWithPath("tableId").description("테이블 아이디"),
					fieldWithPath("operationType").description("작업형식"),
					fieldWithPath("entityData").description("변경정보").optional(), // 상위 필드
					fieldWithPath("entityData.userId").description("사용자 ID"),
					fieldWithPath("entityData.userPassword").description("사용자 비밀번호"),
					fieldWithPath("entityData.userName").description("사용자 이름"),
					fieldWithPath("entityData.userType").description("사용자 유형"),
					fieldWithPath("entityData.createdAt").description("생성 일시"),
					fieldWithPath("entityData.modifiedAt").description("수정 일시"),
					fieldWithPath("entityData.createdBy").description("생성자"),
					fieldWithPath("entityData.modifiedBy").description("수정자"),
					fieldWithPath("entityData.deletedYn").description("삭제 여부"),
					fieldWithPath("createdBy").description("생성자")),
				responseFields( // 응답 필드 정보 입력
					fieldWithPath("id").description("히스토리 아이디"),
					fieldWithPath("tableName").description("테이블명"),
					fieldWithPath("tableId").description("테이블 아이디"),
					fieldWithPath("operationType").description("작업형식"),
					fieldWithPath("changedData").description("변경정보"),
					fieldWithPath("createdAt").description("생성일시"),
					fieldWithPath("createdBy").description("생성자"),
					fieldWithPath("deletedYn").description("삭제여부")
				))
			);
	}

}
