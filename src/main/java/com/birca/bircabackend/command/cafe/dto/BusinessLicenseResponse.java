package com.birca.bircabackend.command.cafe.dto;

public record BusinessLicenseResponse(
        String cafeName,
        String businessLicenseNumber,
        String owner,
        String address,
        Integer uploadCount
) {
    public static BusinessLicenseResponse of(BusinessLicenseInfoResponse businessLicenseInfoResponse,
                                             UploadCountResponse uploadCountResponse) {
        return new BusinessLicenseResponse(
                businessLicenseInfoResponse.cafeName(),
                businessLicenseInfoResponse.businessLicenseNumber(),
                businessLicenseInfoResponse.owner(),
                businessLicenseInfoResponse.address(),
                uploadCountResponse.uploadCount()
        );
    }
}
