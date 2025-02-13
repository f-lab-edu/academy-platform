package spring.academyPlatform.global.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// @Pattern 어노테이션을 사용하여 @RequestParam을 검증할 때, 위반 시 발생하는 예외는 ConstraintViolationException 로 받는다.
	@ExceptionHandler(ConstraintViolationException.class)
	protected ResponseEntity<String> constraintViolationException(ConstraintViolationException ex) {
		log.error(ex.getMessage());
		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body("파라미터 검증에서 오류가 발생하였습니다.");
	}

	// @Valid 통해 검증시 위반사항이 발생하여 이를 받는 예외처리
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<String> processValidationError(MethodArgumentNotValidException e) {
		log.error(e.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body("유효성 검사에서 문제가 발생하엿습니다.");
	}

	/**
	 *  예상하지 못한 예외를 처리하기 위해 지정함.
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<String> exception(Exception e) {
		log.error(e.getMessage());
		return ResponseEntity
			.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body("서버 측 오류가 발생하였습니다.");
	}
}
