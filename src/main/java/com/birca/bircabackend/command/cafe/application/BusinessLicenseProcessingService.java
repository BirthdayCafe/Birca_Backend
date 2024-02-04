package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseOcrClient;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseVerificationClient;
import com.birca.bircabackend.command.cafe.infrastructure.OcrRequestCounter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BusinessLicenseProcessingService {

    private final OcrRequestCounter ocrRequestCounter;
    private final BusinessLicenseOcrClient businessLicenseOcrClient;
    private final BusinessLicenseVerificationClient businessLicenseVerificationClient;

    public BusinessLicenseResponse readBusinessLicense(LoginMember loginMember, MultipartFile businessLicense) {
        ocrRequestCounter.incrementOcrRequestCount(loginMember.id());
        return businessLicenseOcrClient.readBusinessLicense(businessLicense);
    }

    public void verifyBusinessLicenseStatus(String businessLicenseNumber) {
        businessLicenseVerificationClient.verifyBusinessLicenseStatus(businessLicenseNumber);
    }
}
