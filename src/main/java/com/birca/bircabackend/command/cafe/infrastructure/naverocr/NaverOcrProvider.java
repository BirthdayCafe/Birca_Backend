package com.birca.bircabackend.command.cafe.infrastructure.naverocr;

import com.birca.bircabackend.command.cafe.application.OcrProvider;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseInfoResponse;
import com.birca.bircabackend.common.ApiResponseExtractor;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Base64.getEncoder;
import static java.util.UUID.randomUUID;

@Component
@RequiredArgsConstructor
public class NaverOcrProvider implements OcrProvider {

    @Value("${ocr.secret-key}")
    private String secretKey;

    private final NaverOcrApi naverOcrApi;

    @Override
    public BusinessLicenseInfoResponse getBusinessLicenseInfo(MultipartFile businessLicense) {
        BusinessLicenseOcrRequest request = createRequestBody(businessLicense);
        ResponseEntity<BusinessLicenseOcrResponse> response
                = naverOcrApi.performBusinessLicenseOcr(request, secretKey);
        BusinessLicenseOcrResponse body = ApiResponseExtractor.getBody(response);
        return BusinessLicenseInfoResponse.from(body);
    }

    private BusinessLicenseOcrRequest createRequestBody(MultipartFile businessLicense) {
        return new BusinessLicenseOcrRequest(
                "V2",
                randomUUID().toString(),
                currentTimeMillis(),
                List.of(createImageInfo(businessLicense))
        );
    }

    private BusinessLicenseOcrRequest.ImageInfo createImageInfo(MultipartFile businessLicense) {
        try {
            return new BusinessLicenseOcrRequest.ImageInfo(
                    "pdf",
                    getEncoder().encodeToString(businessLicense.getBytes()),
                    businessLicense.getOriginalFilename()
            );
        } catch (IOException e) {
            throw BusinessException.from(new InternalServerErrorCode("사업자등록증 파일 처리 중 오류가 발생했습니다."));
        }
    }
}
