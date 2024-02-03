package com.birca.bircabackend.command.cafe.infrastructure;

public interface BusinessLicenseVerificationClient {
    void verifyBusinessLicenseStatus(String businessLicenseNumber);
}
