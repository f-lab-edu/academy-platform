package spring.academyPlatform.user.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.dto.UserInsertParamDto;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // rest docs 자동 설정
@ActiveProfiles("test") // 'test' 프로파일 활성화
class UserControllerTest extends AbstractIntegrationTest {
	@Autowired
	MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private UserRepository userRepository;

	@Test
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
					fieldWithPath("status").description("응답 상태 코드"), fieldWithPath("message").description("응답 메시지"),
					fieldWithPath("data.userId").description("유저 아이디"),
					fieldWithPath("data.userName").description("유저 이름"),
					fieldWithPath("data.userType").description("유저 타입"),
					fieldWithPath("data.createdBy").description("생성자"),
					fieldWithPath("data.deletedYn").description("삭제여부"))));
	}

	@Test
	void login_user() throws Exception {

		// 사용자 생성
		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId("test9")
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user").contentType(MediaType.APPLICATION_JSON)
			.content(objectMapper.writeValueAsString(dto))).andExpect(status().isOk());

		this.mockMvc.perform(post("/api/v1/user/login-user")
				.contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("userId", "test9")
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

}
