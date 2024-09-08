package com.birca.bircabackend.command.auth.authorization;

import com.birca.bircabackend.command.auth.exception.AuthErrorCode;
import com.birca.bircabackend.command.auth.application.token.JwtTokenExtractor;
import com.birca.bircabackend.command.auth.application.token.JwtTokenProvider;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RequiredLoginInterceptor implements HandlerInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            return handleHandlerMethod(request, (HandlerMethod) handler);
        }
        return true;
    }

    private boolean handleHandlerMethod(HttpServletRequest request, HandlerMethod handler) {
        RequiredLogin requiredLogin = handler.getMethodAnnotation(RequiredLogin.class);
        if (Objects.isNull(requiredLogin)) {
            return true;
        }
        String token = getToken(request);
        validateToken(token);
        request.setAttribute("payload", jwtTokenProvider.getPayload(token));
        return true;
    }

    private String getToken(HttpServletRequest request) {
        return JwtTokenExtractor.extractToken(Objects.requireNonNull(request))
                .orElseThrow(() -> BusinessException.from(AuthErrorCode.NOT_EXISTS_TOKEN));
    }

    private void validateToken(String token) {
        if (!jwtTokenProvider.isValidAccessToken(token)) {
            throw BusinessException.from(AuthErrorCode.INVALID_EXISTS_TOKEN);
        }
    }
}
