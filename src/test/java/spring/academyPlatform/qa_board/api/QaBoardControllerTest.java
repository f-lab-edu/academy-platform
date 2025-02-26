package spring.academyPlatform.qa_board.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.config.AbstractIntegrationTest;
import spring.academyPlatform.qa_board.dao.QaBoardRepository;
import spring.academyPlatform.qa_board.domain.QaBoard;
import spring.academyPlatform.qa_board.dto.QaBoardCreateRequest;
import spring.academyPlatform.qa_board.dto.QaBoardUpdateRequest;
import spring.academyPlatform.user.dao.UserRepository;
import spring.academyPlatform.user.domain.User;
import spring.academyPlatform.user.model.UserTypeCode;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs // rest docs 자동 설정
@ActiveProfiles("test") // 'test' 프로파일 활성화
@Slf4j
@ImportAutoConfiguration(exclude = {
	RedisAutoConfiguration.class,
	RedisHttpSessionConfiguration.class
})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class) // 테스트 실행 순서를 지정함.
class QaBoardControllerTest extends AbstractIntegrationTest {

	@Autowired
	MockMvc mockMvc;
	@Autowired
	private QaBoardRepository qaBoardRepository;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private UserRepository userRepository;
	private MockHttpSession session;
	private QaBoard board;

	@BeforeEach
	void setUp() {

		session = new MockHttpSession(); //1. 세션 생성
		User testUser = User.builder()
			.userId("testId")
			.userName("test_name")
			.userType(UserTypeCode.STUDENT)
			.build();
		userRepository.save(testUser);
		session.setAttribute("user", testUser.getUserName());

		board = QaBoard.builder()
			.title("테스트 게시글")
			.post("테스트 게시글 본문")
			.userId("testId")
			.createdBy(testUser.getUserId())
			.build();

		qaBoardRepository.save(board);
		log.info("before test setting");
	}

	@Test
	@DisplayName("게시글 생성 테스트")
	@Transactional
	@Order(1)
	void create_board() throws Exception {

		QaBoardCreateRequest request = QaBoardCreateRequest.builder()
			.title("게시글 제목1")
			.post("게시글 본문")
			.build();

		mockMvc.perform(post("/api/v1/qa-board/board")
				.session(session)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("create_board",
				requestFields(
					fieldWithPath("title").description("제목"),
					fieldWithPath("post").description("본문")
				),
				responseFields(
					fieldWithPath("boardId").description("게시글 아이디"),
					fieldWithPath("userId").description("사용자 아이디"),
					fieldWithPath("title").description("게시글 제목"),
					fieldWithPath("post").description("게시글 본문"),
					fieldWithPath("createdAt").attributes(key("type").value("LocalDateTime")).description("생성일시"),
					fieldWithPath("createdBy").description("생성자"),
					fieldWithPath("deletedYn").description("삭제여부")
				)
			));
	}

	@Test
	@DisplayName("게시글 목록 조회 테스트")
	@Transactional
	@Order(2)
	void search_boards() throws Exception {

		MultiValueMap<String, String> info = new LinkedMultiValueMap<>();
		info.add("title", "");
		info.add("userId", "");
		info.add("startDate", "20250101");
		info.add("endDate", "20251231");
		info.add("page", "1");
		info.add("size", "10");

		mockMvc.perform(get("/api/v1/qa-board/boards")
				.params(info)
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("search_boards",
				queryParameters(
					parameterWithName("title").description("제목"),
					parameterWithName("userId").description("사용자 아이디"),
					parameterWithName("startDate").description("시작일"),
					parameterWithName("endDate").description("종료일"),
					parameterWithName("page").description("페이지"),
					parameterWithName("size").description("페이지 블럭")
				),
				responseFields(
					fieldWithPath("content").description("게시글 리스트"),
					fieldWithPath("content[].boardId").description("게시글 아이디"),
					fieldWithPath("content[].userId").optional().description("사용자 아이디"),
					fieldWithPath("content[].title").description("게시글 제목"),
					fieldWithPath("content[].post").description("게시글 본문"),
					fieldWithPath("content[].createdAt").attributes(key("type").value("LocalDateTime"))
						.description("생성일시"),
					fieldWithPath("content[].modifiedAt").attributes(key("type").value("LocalDateTime"))
						.description("수정일시"),
					fieldWithPath("content[].createdBy").attributes(key("type").value("String")).description("생성자"),
					fieldWithPath("content[].modifiedBy").attributes(key("type").value("String")).description("수정자"),
					fieldWithPath("content[].deletedYn").description("삭제여부"),

					// 필드가 null 일 수 있다면 선택적으로 표시화 하여 문서화 하기 위해 optional 사용함.
					fieldWithPath("content[].comments").optional()
						.description("댓글리스트(비어있을 수 있음"),

					fieldWithPath("content[].comments[]").optional()
						.description("대댓글리스트(비어있을 수 있음)"),

					fieldWithPath("pageNumber").description("현재 페이지 번호"),
					fieldWithPath("pageSize").description("페이지 크기"),
					fieldWithPath("totalElements").description("전체 데이터 수"),
					fieldWithPath("totalPages").description("전체 페이지 수"),
					fieldWithPath("last").description("마지막 페이지 여부")
				)
			));
	}

	@Test
	@DisplayName("게시글 목록 상세 조회")
	@Transactional
	@Order(3)
	void search_detail_board() throws Exception {

		mockMvc.perform(get("/api/v1/qa-board/board?boardId=" + board.getBoardId())
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("search_board",
				queryParameters(
					parameterWithName("boardId").description("게시글 아이디")
				),
				responseFields(
					fieldWithPath("boardId").description("게시글 아이디"),
					fieldWithPath("userId").description("사용자 아이디"),
					fieldWithPath("title").description("게시글 제목"),
					fieldWithPath("post").description("게시글 본문"),
					fieldWithPath("createdAt").attributes(key("type").value("LocalDateTime")).description("생성일시"),
					fieldWithPath("modifiedAt").attributes(key("type").value("LocalDateTime")).description("수정일시"),
					fieldWithPath("createdBy").description("생성자"),
					fieldWithPath("modifiedBy").attributes(key("type").value("String")).description("수정자"),
					fieldWithPath("deletedYn").description("삭제여부"),
					fieldWithPath("comments").type(JsonFieldType.VARIES).optional().description("댓글 목록")
				)
			));
	}

	@Test
	@DisplayName("게시글 수정 테스트")
	@Transactional
	@Order(4)
	void update_board() throws Exception {

		QaBoardUpdateRequest request = QaBoardUpdateRequest.builder()
			.title("테스트 게시글 수정")
			.post("테스트 게시글 수정")
			.build();

		mockMvc.perform(put("/api/v1/qa-board/board?boardId=" + board.getBoardId())
				.session(session)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
			.andExpect(status().isOk())
			.andDo(document("update_board",
				requestFields(
					fieldWithPath("title").description("제목"),
					fieldWithPath("post").description("본문")
				),
				responseFields(
					fieldWithPath("boardId").description("게시글 seq"),
					fieldWithPath("userId").description("유저 아이디"),
					fieldWithPath("title").description("게시글 제목"),
					fieldWithPath("post").description("게시글 본문"),
					fieldWithPath("createdAt").description("생성일시"),
					fieldWithPath("createdBy").description("생성자"),
					fieldWithPath("modifiedBy").description("수정자"),
					fieldWithPath("modifiedAt").attributes(key("type").value("LocalDateTime")).description("수정일시"),
					fieldWithPath("deletedYn").description("삭제여부")
				)
			));
	}

	@Test
	@DisplayName("게시글 삭제 테스트")
	@Transactional
	@Order(5)
	void delete_board() throws Exception {

		mockMvc.perform(delete("/api/v1/qa-board/board?boardId=" + board.getBoardId())
				.session(session))
			.andExpect(status().isOk())
			.andDo(document("delete_board",
					queryParameters(
						parameterWithName("boardId")
							.description("삭제할 게시글의 ID")
					),
					responseFields(
						fieldWithPath("boardId").description("게시글 seq"),
						fieldWithPath("userId").description("유저 아이디"),
						fieldWithPath("title").description("게시글 제목"),
						fieldWithPath("post").description("게시글 본문"),
						fieldWithPath("modifiedBy").description("수정자"),
						fieldWithPath("modifiedAt").attributes(key("type").value("LocalDateTime")).description("수정일시"),
						fieldWithPath("deletedYn").description("삭제여부")
					)
				)
			);
	}

}
