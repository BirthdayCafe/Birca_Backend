package com.birca.bircabackend.command.cafe.dto;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.BusinessLicenseInfo;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.Result;

import java.util.List;

public record BusinessLicenseResponse(
        String cafeName,
        String businessLicenseNumber,
        String owner,
        String address
) {
    public static BusinessLicenseResponse from(BusinessLicenseOcrResponse businessLicenseOcrResponse) {
        Result result = businessLicenseOcrResponse.images().get(0).bizLicense().result();

        String cafeName = getBusinessLicenseInfo(result.companyName());
        String owner = getBusinessLicenseInfo(result.repName());
        String businessLicenseNumber = getBusinessLicenseInfo(result.registerNumber());
        String address = getBusinessLicenseInfo(result.bisAddress());

        return new BusinessLicenseResponse(
                cafeName,
                businessLicenseNumber,
                owner,
                address
        );
    }

    private static String getBusinessLicenseInfo(List<BusinessLicenseInfo> businessLicenseInfos) {
        return businessLicenseInfos == null ? "" : businessLicenseInfos.get(0).text();
    }
}
