package spring.academyPlatform.user.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.dto.UserInsertParamDto;

/**
 * 테스트 코드는 클래스 전체적으로 실행하면 테이블 공유
 * 개별적으로 사용하면 공유하지 않는다.
 * @SpringBootTest
 * 통합 테스트를 제공하는 기본적인 스프링 부트 테스트 어노테이션
 * @AutoConfigureMockMvc
 * 서블릿 컨테이너를 모킹하기 위해 사용합니다.
 * 웹 환경에서 컨트롤러를 테스트 하려면 반드시 서블릿 컨테이너가 구동되고,
 * DispatcherServlet 객체가 메모리에 올라가야 하지만,
 * 서블릿 컨테이너를 모킹하면 실제 서블릿 컨테이너가 아닌 테스트용 모형 컨테이너를 사용하기 때문에 간단하게 컨트롤러를 테스트 할 수있습니다.
 * @AutoConfigureRestDocs // rest docs 자동 설정
 * @ActiveProfiles("test") // 'test' 프로파일 활성화
 * @TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
 * @Order() 테스트 코드 실행 순서를 지정함.
 *
 */

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // rest docs 자동 설정
@ActiveProfiles("test") // 'test' 프로파일 활성화
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class) // 테스트 코드 실행 순서를 지정함.
@Transactional
class UserControllerTest extends AbstractIntegrationTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository; // 리포지토리 주입 추가

	@BeforeEach
	void setUp() {
		// 모든 유저 데이터 삭제
		userRepository.deleteAll();
	}

	@Test
	@Order(1)
	@DisplayName("회원 가입 테스트")
	void join_user() throws Exception {
		String id = "test9";

		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(document("join_user", // 문서 조각 디렉토리 명
				requestFields( // 요청 본문 필드 정보 입력
					fieldWithPath("userId").description("유저 아이디"),
					fieldWithPath("userPassword").description("유저 비밀번호"),
					fieldWithPath("userName").description("유저 이름"),
					fieldWithPath("userType").description("유저 타입")),
				responseFields( // 응답 필드 정보 입력
					fieldWithPath("status").description("응답 상태 코드"),
					fieldWithPath("message").description("응답 메시지"),
					fieldWithPath("data.userId").description("유저 아이디"),
					fieldWithPath("data.userName").description("유저 이름"),
					fieldWithPath("data.userType").description("유저 타입"),
					fieldWithPath("data.createdBy").description("생성자"),
					fieldWithPath("data.deletedYn").description("삭제여부"))));
	}

	@Test
	@Order(2)
	@DisplayName("중복된 아이디로 가입시 오류발생 테스트")
	void join_user_with_duplicate_id() throws Exception {
		String id = "test100";
		// 사용자 생성
		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		// 중복된 ID 생성 요청 DTO
		UserInsertParamDto dto2 = UserInsertParamDto.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto2)))
			.andExpect(status().isConflict()) // 409 Conflict 기대
			.andExpect(jsonPath("$.status").value(409))
			.andExpect(jsonPath("$.message").value("User ID already exists"))
			.andDo(document("join_user_fail_duplicate_id",
				requestFields(
					fieldWithPath("userId").description("유저 아이디"),
					fieldWithPath("userPassword").description("유저 비밀번호"),
					fieldWithPath("userName").description("유저 이름"),
					fieldWithPath("userType").description("유저 타입")
				),
				responseFields(
					fieldWithPath("status").description("HTTP 상태 코드"),
					fieldWithPath("message").description("에러 메시지"),
					fieldWithPath("data").description("리턴값 없음")
				)
			));
	}

	@Test
	@Order(3)
	@DisplayName("로그인 성공 테스트")
	void login_success() throws Exception {

		// 사용자 생성
		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId("test11")
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		mockMvc.perform(post("/api/v1/user/login-user")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test11")
				.param("password", "password"))
			.andExpect(status().isOk())
			.andDo(document("login_user", formParameters(parameterWithName("userId").description("User ID"),
						parameterWithName("password").description("User Password")),
					responseFields( // 응답 필드 명세 추가
						fieldWithPath("status").description("응답 상태 코드"),
						fieldWithPath("message").description("응답 메시지"),
						fieldWithPath("data").description("로그인 성공 여부")
					)
				)
			);
	}

	@Test
	@Order(4)
	@DisplayName("잘못된 아이디 입력시 오류발생")
	void login_failed_with_id() throws Exception {

		// 사용자 생성
		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId("test12")
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		mockMvc.perform(post("/api/v1/user/login-user")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test10000")
				.param("password", "password"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("해당 아이디는 존재하지 않습니다."))
			.andDo(document("login_user_failed_id",
				formParameters(
					parameterWithName("userId").description("User ID"),
					parameterWithName("password").description("User Password")
				),
				responseFields(
					fieldWithPath("status").description("응답 상태 코드"),
					fieldWithPath("message").description("응답 메시지"),
					fieldWithPath("data").description("로그인 성공 여부")
				)
			));

	}

	@Test
	@Order(5)
	@DisplayName("아이디에 해당하는 비밀번호가 아닐때 오류발생")
	void login_failed_with_password() throws Exception {

		// 사용자 생성
		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId("test200")
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		mockMvc.perform(post("/api/v1/user/login-user")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test200")
				.param("password", "password123"))
			.andExpect(status().isBadRequest())
			.andExpect(jsonPath("$.status").value(400))
			.andExpect(jsonPath("$.message").value("입력한 정보가 올바르지 않습니다"))
			.andDo(document("login_user_failed_password",
				formParameters(
					parameterWithName("userId").description("User ID"),
					parameterWithName("password").description("User Password")
				),
				responseFields(
					fieldWithPath("status").description("응답 상태 코드"),
					fieldWithPath("message").description("응답 메시지"),
					fieldWithPath("data").description("로그인 성공 여부")
				)
			));

	}

}
