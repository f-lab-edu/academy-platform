package spring.academyPlatform.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.util.Message;
import spring.academyPlatform.user.application.UserService;
import spring.academyPlatform.user.dto.UserInsertParamDto;
import spring.academyPlatform.user.dto.UserInsertResponseDto;

/**
 * @RestController
 * 객체만을 반환하는 컨트롤러입니다. 객체 데이터는 json, 또는 xml 형식으로 HTTP 응답에 담아 전송합니다.
 * @RequestMapping("api/v1/user")
 * Spring 개발 시 특정 URL로 요청(Request)을 보내면 Controller에서 어떠한 방식으로 처리할지 정의합니다.
 * 이때 들어온 요청을 특정 method와 매핑하기 위해 사용하는 어노테이션이 바로 @RequestMapping입니다.
 * 컨트롤러 단에서는 공통된 URL로 묶어주고 @GetMapping ,@PostMapping등을 사용해서 메서드와 매핑해줍니다.
 * @RequiredArgsConstructor
 *  final 키워드가 붙거나 @NonNull 어노테이션이 붙은 필드에 대해 생성자를 자동으로 생성하여 의존성 주입 과정을 단순화 합니다.
 * @Slf4j
 * 개발자로 하여금 SLF4J를 사용하기 위해 작성해야 하는 코드(Boilerplate code)를 생략할 수 있게끔 해준다
 * SLF4J는 여러 로깅 프레임워크(java.util.logging, logback, Log4j) 의 추상화를 제공해주는 라이브러리입니다.
 * @Valid
 * 빈 검증기(Bean Validator)를 이용해 객체의 제약 조건을 검증하도록 지시하는 어노테이션입니다.
 * 유효성 검사가 필요한 Request 객체에 Valid 어노테이션을 사용해 유효성 검사를 적용할 수 있다
 * 기본적으로 컨트롤러에서만 동작합니다.
 * @RequestBody
 * 해당 어노테이션이 붙은 파라미터에는 HTTP 요청의 분문 body 부분이 그대로 전달된다
 * body에 담긴 json, xml등의 리소스를 역직렬화 해줍니다.
 */
@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;

	@PostMapping("/join-user")
	public ResponseEntity<Message> insertUser(@Valid @RequestBody UserInsertParamDto dto) {
		try {
			UserInsertResponseDto result = userService.insertUser(dto);
			return ResponseEntity.status(HttpStatus.OK)
				.body(new Message(HttpStatus.OK, "Successfully joined user!", result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.CONFLICT)
				.body(new Message(HttpStatus.CONFLICT, e.getMessage()));
		}
	}

	@PostMapping("/login-user")
	public ResponseEntity<Message> getLoginUser(@RequestParam String userId, @RequestParam String password,
		HttpSession session) {
		try {
			boolean result = userService.authenticate(userId, password, session);
			return ResponseEntity.status(HttpStatus.OK)
				.body(new Message(HttpStatus.OK, "Successfully login!", result));

		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(new Message(HttpStatus.BAD_REQUEST, e.getMessage()));
		}
	}

	/**
	 * HttpSession , HttpServletRequest/response
	 *
	 *
	 */
	@PostMapping("/logout")
	public ResponseEntity<String> logoutUser(HttpSession session) {
		if (session != null) {
			session.invalidate();
		} else {
			throw new IllegalStateException("로그인 상태가 아닙니다.");
		}
		return ResponseEntity.status(HttpStatus.OK).body("Successfully logged out!");
	}

}
