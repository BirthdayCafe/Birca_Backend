package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BusinessLicenseProcessingService {

    private final OcrProvider ocrProvider;
    private final VerificationProvider verificationProvider;

    public BusinessLicenseResponse getBusinessLicenseInfo(MultipartFile businessLicense) {
        return ocrProvider.getBusinessLicenseInfo(businessLicense);
    }

    public void verifyBusinessLicenseStatus(String businessLicenseNumber) {
        verificationProvider.verifyBusinessLicenseStatus(businessLicenseNumber);
    }
}
