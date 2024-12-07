package spring.academyPlatform.global.util;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class BcryptPasswordEncryptor {

	static int cost = 8;  // 반복 횟수 (보통 10~12 권장)

	// Bcrypt를 사용해 비밀번호를 해싱하는 메서드
	public static String hashPassword(String password) {

		return BCrypt.hashpw(password, BCrypt.gensalt(cost));
	}

	// 해싱된 비밀번호와 사용자가 입력한 비밀번호를 검증하는 메서드
	public static boolean checkPassword(String password, String hashedPassword) {
		return BCrypt.checkpw(password, hashedPassword);
	}

}
