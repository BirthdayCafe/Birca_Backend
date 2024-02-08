package com.birca.bircabackend.command.cafe.validation;

import com.birca.bircabackend.command.auth.application.token.TokenPayload;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Objects;
import java.util.Optional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;

@Component
@RequiredArgsConstructor
public class OcrRequestLimitInterceptor implements HandlerInterceptor {

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;
    private static final int MAX_UPLOAD_LIMIT = 5;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (CorsUtils.isPreFlightRequest(request)) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            return handleHandlerMethod(request, (HandlerMethod) handler);
        }
        return true;
    }

    private boolean handleHandlerMethod(HttpServletRequest request, HandlerMethod handler) {
        UploadCountCheck uploadCountCheck = handler.getMethodAnnotation(UploadCountCheck.class);
        if (Objects.isNull(uploadCountCheck)) {
            return true;
        }
        TokenPayload payload = getPayload(request);
        ocrRequestHistoryRepository.findUploadCountByOwnerId(payload.id())
                .ifPresent(this::validateUploadCount);
        return true;
    }

    private void validateUploadCount(Integer uploadCount) {
        if (uploadCount >= MAX_UPLOAD_LIMIT) {
            throw BusinessException.from(OVER_MAX_OCR_REQUEST_COUNT);
        }
    }

    private TokenPayload getPayload(HttpServletRequest request) {
        return Optional.ofNullable((TokenPayload) request.getAttribute("payload"))
                .orElseThrow(() -> BusinessException.from(new InternalServerErrorCode("토큰이 전달되지 않은 요청입니다.")));
    }
}
