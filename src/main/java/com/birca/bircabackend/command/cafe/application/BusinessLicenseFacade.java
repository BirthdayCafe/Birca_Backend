package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BusinessLicenseFacade {

    private final BusinessLicenseVerificationService businessLicenseVerificationService;
    private final BusinessLicenseService businessLicenseService;

    public void save(LoginMember loginMember, BusinessLicenseCreateRequest request) {
        String businessLicenseNumber = request.businessLicenseNumber();
        businessLicenseVerificationService.verifyBusinessLicenseStatus(businessLicenseNumber);
        businessLicenseService.saveBusinessLicense(loginMember, request);
    }
}
