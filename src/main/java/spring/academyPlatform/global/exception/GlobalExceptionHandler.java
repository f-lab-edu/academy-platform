package spring.academyPlatform.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// @Pattern 어노테이션을 사용하여 @RequestParam을 검증할 때, 위반 시 발생하는 예외는 ConstraintViolationException 로 받는다.
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<String> constraintViolationException(ConstraintViolationException ex) {
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ex.getMessage());
	}

	/**
	 *  예상하지 못한 예외를 처리하기 위해 지정함.
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> exception(Exception e) {
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(e.getMessage());
	}
}
