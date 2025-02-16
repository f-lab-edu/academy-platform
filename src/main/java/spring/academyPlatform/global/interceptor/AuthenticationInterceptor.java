package spring.academyPlatform.global.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthenticationInterceptor implements HandlerInterceptor {
	// 컨트롤러 진입 전에 실행되는 메서드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		String requestURI = request.getRequestURI();
		String method = request.getMethod();

		// "/error" 경로는 인터셉터에서 제외
		if (requestURI.equals("/error")) {
			return true;
		}

		log.error("preHandle triggered - Method: {}, URI: {}", method, requestURI);

		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("user") != null) {
			// 세션에 사용자 정보가 있다면 인증된 상태
			return true;
		}
		// 인증 정보가 없다면
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
		log.error("error {}", HttpServletResponse.SC_UNAUTHORIZED);
		return false;
	}
}
