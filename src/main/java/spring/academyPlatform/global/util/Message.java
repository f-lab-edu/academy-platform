package spring.academyPlatform.global.util;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Message {
	private int status;
	private String message;
	private Object data;

	public Message(HttpStatus httpStatus, String message, Object data) {
		this.status = httpStatus.value();
		this.message = message;
		this.data = data;
	}

	public Message(HttpStatus httpStatus, String message) {
		this.status = httpStatus.value();
		this.message = message;
	}
}
