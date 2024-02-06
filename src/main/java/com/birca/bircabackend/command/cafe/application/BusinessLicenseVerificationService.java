package com.birca.bircabackend.command.cafe.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BusinessLicenseVerificationService {

    private final VerificationProvider verificationProvider;

    public void verifyBusinessLicenseStatus(String businessLicenseNumber) {
        verificationProvider.verifyBusinessLicenseStatus(businessLicenseNumber);
    }
}
