package spring.academyPlatform.qa_comment.api;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.session.data.redis.config.annotation.web.http.RedisHttpSessionConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.model.YnCode;
import spring.academyPlatform.qa_board.dao.QaBoardRepository;
import spring.academyPlatform.qa_board.domain.QaBoard;
import spring.academyPlatform.qa_comment.dao.QaCommentRepository;
import spring.academyPlatform.qa_comment.domain.QaComment;
import spring.academyPlatform.qa_comment.dto.QaCommentCreateRequest;
import spring.academyPlatform.qa_comment.dto.QaCommentUpdateRequest;
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
public class QaCommentControllerTest {

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
	private QaComment comment;
	@Autowired
	private QaCommentRepository qaCommentRepository;

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
		session.setAttribute("userId", testUser.getUserId());

		board = QaBoard.builder()
			.title("테스트 게시글")
			.post("테스트 게시글 본문")
			.userId("testId")
			.build();

		qaBoardRepository.save(board);

		comment = QaComment.builder()
			.boardId(board.getBoardId())
			.userId("testId")
			.title("댓글 제목")
			.post("댓글 본문")
			.deletedYn(YnCode.N)
			.build();

		qaCommentRepository.save(comment);

		log.info("before test setting");
	}

	@Test
	@DisplayName("댓글 생성 테스트")
	@Transactional
	@Order(1)
	void create_comment() throws Exception {

		QaCommentCreateRequest request = QaCommentCreateRequest.builder()
			.boardId(board.getBoardId())
			.parentCommentId(null)
			.title("댓글 제목")
			.post("댓글 본문")
			.build();

		log.info("session", session.getAttribute("user"));
		mockMvc.perform(post("/api/v1/qa-comment/comment")
				.session(session)
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("create_comment",
					requestFields(
						fieldWithPath("boardId").description("게시글 ID"),
						fieldWithPath("parentCommentId").attributes(key("type").value("String")).description("부모 댓글 ID"),
						fieldWithPath("title").description("제목"),
						fieldWithPath("post").description("본문")
					),
					responseFields(
						fieldWithPath("commentId").description("댓글 아이디"),
						fieldWithPath("boardId").description("게시글 아이디"),
						fieldWithPath("userId").description("사용자 아이디"),
						fieldWithPath("parentsCommentId").attributes(key("type").value("String")).description("부모 댓글 아이디"),
						fieldWithPath("priorityNumber").description("댓글 순번"),
						fieldWithPath("title").description("제목"),
						fieldWithPath("post").description("본문"),
						fieldWithPath("createdAt").attributes(key("type").value("LocalDateTime")).description("생성일시"),
						fieldWithPath("createdBy").description("생성자"),
						fieldWithPath("deletedYn").description("삭제여부")
					)
				)
			);
	}

	@Test
	@DisplayName("댓글 수정 테스트")
	@Transactional
	@Order(2)
	void change_comment() throws Exception {

		QaCommentUpdateRequest request = QaCommentUpdateRequest.builder()
			.title("수정 댓글명")
			.post("수정 댓글 본문")
			.build();

		mockMvc.perform(put("/api/v1/qa-comment/comment")
				.session(session)
				.param("commentId", comment.getCommentId().toString())
				.content(objectMapper.writeValueAsString(request))
				.contentType(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andDo(document("update_comment",
					requestFields(
						fieldWithPath("title").description("제목"),
						fieldWithPath("post").description("본문")
					),
					responseFields(
						fieldWithPath("commentId").description("댓글 아이디"),
						fieldWithPath("boardId").description("게시글 아이디"),
						fieldWithPath("userId").description("사용자 아이디"),
						fieldWithPath("parentsCommentId").attributes(key("type").value("String")).description("부모 댓글 아이디"),
						fieldWithPath("priorityNumber").description("댓글 순번"),
						fieldWithPath("title").description("제목"),
						fieldWithPath("post").description("본문"),
						fieldWithPath("createdAt").attributes(key("type").value("LocalDateTime")).description("생성일시"),
						fieldWithPath("createdBy").description("생성자"),
						fieldWithPath("modifiedBy").description("수정자"),
						fieldWithPath("modifiedAt").attributes(key("type").value("LocalDateTime")).description("수정일시"),
						fieldWithPath("deletedYn").description("삭제여부")
					)
				)
			);
	}

	@Test
	@DisplayName("댓글 삭제 테스트")
	@Transactional
	@Order(3)
	void delete_comment() throws Exception {

		mockMvc.perform(delete("/api/v1/qa-comment/comment?commentId=" + comment.getCommentId())
				.session(session))
			.andExpect(status().isOk())
			.andDo(document("delete_comment",
					queryParameters(
						parameterWithName("commentId")
							.description("댓글 아이디")
					),
					responseFields(
						fieldWithPath("commentId").description("댓글 아이디"),
						fieldWithPath("boardId").description("게시글 아이디"),
						fieldWithPath("userId").description("사용자 아이디"),
						fieldWithPath("parentsCommentId").attributes(key("type").value("String")).description("부모 댓글 아이디"),
						fieldWithPath("priorityNumber").attributes(key("type").value("String")).description("댓글 순번"),
						fieldWithPath("title").description("제목"),
						fieldWithPath("post").description("본문"),
						fieldWithPath("createdAt").attributes(key("type").value("LocalDateTime")).description("생성일시"),
						fieldWithPath("createdBy").description("생성자"),
						fieldWithPath("modifiedBy").description("수정자"),
						fieldWithPath("modifiedAt").attributes(key("type").value("LocalDateTime")).description("수정일시"),
						fieldWithPath("deletedYn").description("삭제여부")
					)
				)
			);
	}

}
