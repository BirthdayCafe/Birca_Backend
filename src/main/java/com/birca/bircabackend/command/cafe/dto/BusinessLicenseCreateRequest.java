package com.birca.bircabackend.command.cafe.dto;

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

    public BusinessLicense toBusinessLicense(Long ownerId, BusinessLicenseCode businessLicenseCode, String imageUrl) {
        return BusinessLicense.createBusinessLicense(
                ownerId,
                owner,
                cafeName,
                businessLicenseCode,
                address,
                imageUrl
        );
    }
}
