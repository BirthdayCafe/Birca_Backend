package com.birca.bircabackend.command.cafe.dto;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import com.birca.bircabackend.command.cafe.domain.BusinessLicenseCode;
import org.springframework.web.multipart.MultipartFile;

public record BusinessLicenseCreateRequest(
        MultipartFile businessLicense,
        String cafeName,
        String businessLicenseNumber,
        String owner,
        String address
) {
    public BusinessLicense toBusinessLicense(LoginMember loginMember, BusinessLicenseCode businessLicenseCode) {
        return BusinessLicense.createBusinessLicense(
                loginMember.id(),
                owner,
                cafeName,
                businessLicenseCode,
                address,
                businessLicense.getOriginalFilename());
    }
}
