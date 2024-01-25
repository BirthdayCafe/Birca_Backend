package com.birca.bircabackend.command.auth.login;

import com.birca.bircabackend.command.auth.exception.AuthErrorCode;
import com.birca.bircabackend.command.auth.token.JwtTokenExtractor;
import com.birca.bircabackend.command.auth.token.JwtTokenProvider;
import com.birca.bircabackend.command.auth.token.TokenPayload;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(LoginMember.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        String accessToken = getAccessToken(request);
        TokenPayload payload = jwtTokenProvider.getPayload(accessToken);
        return new LoginMember(payload.id());
    }

    private String getAccessToken(HttpServletRequest request) {
        return JwtTokenExtractor.extractToken(Objects.requireNonNull(request))
                .orElseThrow(() -> BusinessException.from(AuthErrorCode.NOT_EXISTS_TOKEN));
    }
}
