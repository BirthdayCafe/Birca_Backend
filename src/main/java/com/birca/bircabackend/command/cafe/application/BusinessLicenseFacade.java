package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.common.image.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessLicenseFacade {

    private final BusinessLicenseStatusVerifier businessLicenseStatusVerifier;
    private final BusinessLicenseService businessLicenseService;
    private final ImageRepository imageRepository;

    public void saveBusinessLicense(LoginMember loginMember, BusinessLicenseCreateRequest request) {
        businessLicenseStatusVerifier.verifyBusinessLicenseStatus(request.businessLicenseNumber());
        String imageUrl = imageRepository.upload(request.businessLicense());
        businessLicenseService.saveBusinessLicense(loginMember.id(), request, imageUrl);
    }
}
