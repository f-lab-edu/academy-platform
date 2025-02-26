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
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.model.UserTypeCode;

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

		UserCreateRequest dto = UserCreateRequest.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();

		mockMvc.perform(post("/api/v1/user/user").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(document("join_user", // 문서 조각 디렉토리 명
					requestFields( // 요청 본문 필드 정보 입력
						fieldWithPath("userId").description("유저 아이디"),
						fieldWithPath("userPassword").description("유저 비밀번호"),
						fieldWithPath("userName").description("유저 이름"),
						fieldWithPath("userType").description("유저 타입")),
					responseFields( // 응답 필드 정보 입력
						fieldWithPath("userId").description("사용자 아이디"),
						fieldWithPath("userName").description("사용자 이름"),
						fieldWithPath("userType").description("사용자 유형"),
						fieldWithPath("createdBy").description("생성자"),
						fieldWithPath("deletedYn").description("삭제 여부")
					)
				)
			);
	}

	@Test
	@Order(2)
	@DisplayName("중복된 아이디로 가입시 오류발생 테스트")
	void join_user_with_duplicate_id() throws Exception {
		String id = "test100";
		// 사용자 생성
		UserCreateRequest dto = UserCreateRequest.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();

		mockMvc.perform(post("/api/v1/user/user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		// 중복된 ID 생성 요청 DTO
		UserCreateRequest dto2 = UserCreateRequest.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();

		mockMvc.perform(post("/api/v1/user/user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto2)))
			.andExpect(status().is5xxServerError())
			.andDo(document("join_user_fail_duplicate_id",
					requestFields(
						fieldWithPath("userId").description("유저 아이디"),
						fieldWithPath("userPassword").description("유저 비밀번호"),
						fieldWithPath("userName").description("유저 이름"),
						fieldWithPath("userType").description("유저 타입")
					),
					responseBody()
				)
			);
	}

	@Test
	@Order(3)
	@DisplayName("로그인 성공 테스트")
	void login_success() throws Exception {

		// 사용자 생성
		UserCreateRequest dto = UserCreateRequest.builder()
			.userId("test11")
			.userPassword("password")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();

		mockMvc.perform(post("/api/v1/user/user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		mockMvc.perform(post("/api/v1/user/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test11")
				.param("password", "password"))
			.andExpect(status().isOk())
			.andDo(document("login_user", formParameters(parameterWithName("userId").description("User ID"),
						parameterWithName("password").description("User Password")),
					responseBody()
				)
			);
	}

	@Test
	@Order(4)
	@DisplayName("잘못된 아이디 입력시 오류발생")
	void login_failed_with_id() throws Exception {

		// 사용자 생성
		UserCreateRequest dto = UserCreateRequest.builder()
			.userId("test12")
			.userPassword("password")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();

		mockMvc.perform(post("/api/v1/user/user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		mockMvc.perform(post("/api/v1/user/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test10000")
				.param("password", "password"))
			.andExpect(status().is5xxServerError())
			.andDo(document("login_user_failed_id",
				formParameters(
					parameterWithName("userId").description("User ID"),
					parameterWithName("password").description("User Password")
				),
				responseBody()
			));

	}

	@Test
	@Order(5)
	@DisplayName("아이디에 해당하는 비밀번호가 아닐때 오류발생")
	void login_failed_with_password() throws Exception {

		// 사용자 생성
		UserCreateRequest dto = UserCreateRequest.builder()
			.userId("test200")
			.userPassword("password")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();

		mockMvc.perform(post("/api/v1/user/user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		mockMvc.perform(post("/api/v1/user/login")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test200")
				.param("password", "password123"))
			.andExpect(status().is5xxServerError())
			.andDo(document("login_user_failed_password",
				formParameters(
					parameterWithName("userId").description("User ID"),
					parameterWithName("password").description("User Password")
				),
				responseBody()
			));

	}

}
