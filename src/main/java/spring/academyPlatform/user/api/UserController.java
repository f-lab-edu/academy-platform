package spring.academyPlatform.user.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import spring.academyPlatform.global.util.Message;
import spring.academyPlatform.user.application.UserService;
import spring.academyPlatform.user.dto.UserDto;

@RestController
@RequestMapping("api/v1/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {

	private final UserService userService;

	@PostMapping("/join-user")
	public ResponseEntity<Message> getUser(@Valid @RequestBody UserDto dto) {
		try {
			UserDto result = userService.insertUser(dto);
			return ResponseEntity.status(HttpStatus.OK)
				.body(new Message(HttpStatus.OK, "Successfully joined user!", dto));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new Message(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
		}
	}

	@GetMapping("/find-user")
	public ResponseEntity<Message> findUser(@RequestParam Long id) {
		try {
			UserDto result = userService.findUser(id);
			return ResponseEntity.status(HttpStatus.OK)
				.body(new Message(HttpStatus.OK, "Successfully joined user!", result));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
				.body(new Message(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage()));
		}
	}
}
