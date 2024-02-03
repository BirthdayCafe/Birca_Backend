package com.birca.bircabackend.command.cafe.dto;

import org.springframework.web.multipart.MultipartFile;

public record BusinessLicenseCreateRequest(
        MultipartFile businessLicense,
        String cafeName,
        String businessLicenseNumber,
        String owner,
        String address
) {
}
