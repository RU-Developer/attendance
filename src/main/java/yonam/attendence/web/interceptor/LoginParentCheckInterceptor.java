package yonam.attendence.web.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import yonam.attendence.web.SessionConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginParentCheckInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        HttpSession session = request.getSession();

        if (session == null || session.getAttribute(SessionConst.LOGIN_PARENT) == null) {
            response.sendRedirect("/parents/login?redirectURL=" + requestURI);
            return false;
        }

        return true;
    }
}
