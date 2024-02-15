package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseInfoResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BusinessLicenseFacade {

    private final BusinessLicenseStatusVerifier businessLicenseStatusVerifier;
    private final OcrRequestCountValidator ocrRequestCountValidator;
    private final OcrRequestCountProvider ocrRequestCountProvider;
    private final BusinessLicenseService businessLicenseService;
    private final OcrProvider ocrProvider;

    public BusinessLicenseResponse getBusinessLicenseInfoAndUploadCount(LoginMember loginMember,
                                                                        MultipartFile businessLicense) {
        ocrRequestCountValidator.validateUploadCount(loginMember.id());
        BusinessLicenseInfoResponse businessLicenseInfo = ocrProvider.getBusinessLicenseInfo(businessLicense);
        UploadCountResponse uploadCount = ocrRequestCountProvider.increaseUploadCount(loginMember.id());
        return BusinessLicenseResponse.of(businessLicenseInfo, uploadCount);
    }

    public void saveBusinessLicense(LoginMember loginMember, BusinessLicenseCreateRequest request) {
        businessLicenseStatusVerifier.verifyBusinessLicenseStatus(request.businessLicenseNumber());
        businessLicenseService.saveBusinessLicense(loginMember, request);
    }
}
