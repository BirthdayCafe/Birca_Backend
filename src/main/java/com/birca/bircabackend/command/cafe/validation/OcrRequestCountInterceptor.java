package com.birca.bircabackend.command.cafe.validation;

import com.birca.bircabackend.command.auth.application.token.TokenPayload;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class OcrRequestCountInterceptor implements HandlerInterceptor {

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;
    private final OcrRequestCountValidator ocrRequestCountValidator;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            return handleHandlerMethod(request);
        }
        return true;
    }

    private boolean handleHandlerMethod(HttpServletRequest request) {
        TokenPayload payload = getPayload(request);
        ocrRequestHistoryRepository.findUploadCountByOwnerId(payload.id())
                .ifPresent(ocrRequestCountValidator::validateUploadCount);
        return true;
    }

    private TokenPayload getPayload(HttpServletRequest request) {
        return Optional.ofNullable((TokenPayload) request.getAttribute("payload"))
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("토큰이 전달되지 않은 요청입니다.")));
    }
}
