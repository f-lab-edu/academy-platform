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
import spring.academyPlatform.user.application.UserService;
import spring.academyPlatform.user.dto.UserCreateRequest;
import spring.academyPlatform.user.dto.UserCreateResponse;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;


	@PostMapping("/user")
	public ResponseEntity<UserCreateResponse> getUser(@Valid @RequestBody UserCreateRequest dto) {
		try {
			UserCreateResponse result = userService.createUser(dto);
			return ResponseEntity.status(HttpStatus.OK).body(result);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
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
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new Message(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
		}
	}

}
