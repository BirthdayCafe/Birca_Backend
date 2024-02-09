package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseInfoResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BusinessLicenseFacade {

    private final BusinessLicenseProcessingService businessLicenseProcessingService;
    private final OcrRequestCountProvider ocrRequestCountProvider;

    public BusinessLicenseResponse getBusinessLicenseInfoAndUploadCount(LoginMember loginMember,
                                                                        MultipartFile businessLicense) {
        BusinessLicenseInfoResponse businessLicenseInfo
                = businessLicenseProcessingService.getBusinessLicenseInfo(businessLicense);
        UploadCountResponse uploadCount = ocrRequestCountProvider.getUploadCount(loginMember.id());
        return BusinessLicenseResponse.of(businessLicenseInfo, uploadCount);
    }
}
