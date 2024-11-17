package spring.academyPlatform.user.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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

	// @Autowired
	// private ObjectMapper objectMapper;

	@Test
	void join_user() throws Exception {
		String id = "test9";

		UserInsertParamDto dto = UserInsertParamDto.builder()
			.userId(id)
			.userPassword("password")
			.userName("test_name")
			.userType("student")
			.build();

		mockMvc.perform(post("/api/v1/user/join-user")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
			.andExpect(status().isOk())
			.andDo(document("user-join", // 문서 조각 디렉토리 명
					requestFields( // 요청 본문 필드 정보 입력
						fieldWithPath("userId").description("User ID"),
						fieldWithPath("userPassword").description("User Password"),
						fieldWithPath("userName").description("User Name"),
						fieldWithPath("userType").description("User Type")
					),
					responseFields( // 응답 필드 정보 입력
						fieldWithPath("status").description("응답 상태 코드"),
						fieldWithPath("message").description("응답 메시지"),
						fieldWithPath("data.userId").description("User ID"),
						fieldWithPath("data.userName").description("User Name"),
						fieldWithPath("data.userType").description("User Type"),
						fieldWithPath("data.createdBy").description("Created By"),
						fieldWithPath("data.deletedYn").description("Deleted Yn")
					)
				)
			);
	}
}
