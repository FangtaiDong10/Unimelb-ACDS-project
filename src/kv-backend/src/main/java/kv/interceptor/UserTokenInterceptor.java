package kv.interceptor;

import kv.config.TokenConfig;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserTokenInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String token = request.getHeader(TokenConfig.TOKEN_KEY);
        if (TokenConfig.tokenTimeMap.containsKey(token)) return true;

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        return false;
    }
}
