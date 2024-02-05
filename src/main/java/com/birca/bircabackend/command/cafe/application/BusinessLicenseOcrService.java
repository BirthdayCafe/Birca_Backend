package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrRequest.ImageInfo;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseOcrApi;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static java.lang.System.currentTimeMillis;
import static java.util.Base64.getEncoder;
import static java.util.UUID.randomUUID;

@Service
@RequiredArgsConstructor
public class BusinessLicenseOcrService {

    @Value("${ocr.secret-key}")
    private String secretKey;

    private final BusinessLicenseOcrApi ocrFeignClient;

    public BusinessLicenseResponse readBusinessLicense(MultipartFile businessLicense) {
        BusinessLicenseOcrRequest request = createRequestBody(businessLicense);
        BusinessLicenseOcrResponse ocrResponse = ocrFeignClient.performBusinessLicenseOcr(request, secretKey);
        return BusinessLicenseResponse.from(ocrResponse);
    }

    private BusinessLicenseOcrRequest createRequestBody(MultipartFile businessLicense) {
        return new BusinessLicenseOcrRequest(
                "V2",
                randomUUID().toString(),
                currentTimeMillis(),
                List.of(createImageInfo(businessLicense))
        );
    }

    private ImageInfo createImageInfo(MultipartFile businessLicense) {
        try {
            return new ImageInfo(
                    "pdf",
                    getEncoder().encodeToString(businessLicense.getBytes()),
                    businessLicense.getOriginalFilename()
            );
        } catch (IOException e) {
            throw BusinessException.from(new InternalServerErrorCode("사업자등록증 파일 처리 중 오류가 발생했습니다."));
        }
    }
}
