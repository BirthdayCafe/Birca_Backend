package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_BUSINESS_LICENSE_OR_IMAGE_PROCESSING_FAILED;
import static java.lang.System.currentTimeMillis;
import static java.util.Base64.getEncoder;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class BusinessLicenseOcrClientImpl implements BusinessLicenseOcrClient {

    private final WebClient businessLicenseOcrClient;
    @Value("${ocr.secret-key}")
    private String secretKey;

    @Override
    public BusinessLicenseResponse readBusinessLicense(MultipartFile businessLicense) {
        Map<String, Object> requestBody = createRequestBody(businessLicense);
        BusinessLicenseOcrResponse businessLicenseOcrResponse = performBusinessLicenseOcr(requestBody);
        return BusinessLicenseResponse.from(businessLicenseOcrResponse);
    }

    private BusinessLicenseOcrResponse performBusinessLicenseOcr(Map<String, Object> requestBody) {
        return businessLicenseOcrClient.post()
                .header("X-OCR-SECRET", secretKey)
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(BusinessLicenseOcrResponse.class)
                .onErrorMap(error -> BusinessException.from(NOT_BUSINESS_LICENSE_OR_IMAGE_PROCESSING_FAILED))
                .block();
    }

    private Map<String, Object> createRequestBody(MultipartFile businessLicense) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("version", "V2");
        requestBody.put("requestId", randomUUID().toString());
        requestBody.put("timestamp", currentTimeMillis());
        requestBody.put("images", Collections.singletonList(createImageInfo(businessLicense)));
        return requestBody;
    }

    private Map<String, String> createImageInfo(MultipartFile businessLicense) {
        try {
            Map<String, String> imageInfo = new HashMap<>();
            imageInfo.put("format", "pdf");
            imageInfo.put("data", getEncoder().encodeToString(businessLicense.getBytes()));
            imageInfo.put("name", businessLicense.getOriginalFilename());
            return imageInfo;
        } catch (IOException e) {
            throw BusinessException.from(new InternalServerErrorCode("사업자등록증 파일 처리 중 오류가 발생했습니다."));
        }
    }
}
