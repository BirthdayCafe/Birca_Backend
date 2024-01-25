package com.birca.bircabackend.command.auth.login;

import com.birca.bircabackend.command.auth.token.TokenPayload;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Optional;

@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

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
        assert request != null;
        TokenPayload payload = getPayload(request);
        return new LoginMember(payload.id());
    }

    private TokenPayload getPayload(HttpServletRequest request) {
        return Optional.ofNullable((TokenPayload) request.getAttribute("payload"))
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("토큰이 전달되지 않은 요청입니다.")));
    }
}
