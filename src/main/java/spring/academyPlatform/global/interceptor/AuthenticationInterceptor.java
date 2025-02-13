package spring.academyPlatform.global.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class AuthenticationInterceptor implements HandlerInterceptor {
	// 컨트롤러 진입 전에 실행되는 메서드
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws
		Exception {

		// 예: 로그인 없이 접근 가능한 경로는 예외 처리
		String requestURI = request.getRequestURI();
		if (requestURI.startsWith("/join/")) {
			// "/auth/**" 경로는 인증 체크 없이 통과 (로그인, 로그아웃 요청 등)
			return true;
		}

		HttpSession session = request.getSession(false);
		if (session != null && session.getAttribute("user") != null) {
			// 세션에 사용자 정보가 있다면 인증된 상태
			return true;
		}

		// 인증 정보가 없다면
		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
		return false;
	}
}
