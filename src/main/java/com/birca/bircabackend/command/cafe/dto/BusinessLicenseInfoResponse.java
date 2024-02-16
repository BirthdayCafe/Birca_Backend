package com.birca.bircabackend.command.cafe.dto;

import com.birca.bircabackend.command.cafe.infrastructure.naverocr.BusinessLicenseOcrResponse;
import com.birca.bircabackend.command.cafe.infrastructure.naverocr.BusinessLicenseOcrResponse.Image.BizLicense.BusinessLicenseInfo;
import com.birca.bircabackend.command.cafe.infrastructure.naverocr.BusinessLicenseOcrResponse.Image.BizLicense.Result;

import java.util.List;

public record BusinessLicenseInfoResponse(
        String cafeName,
        String businessLicenseNumber,
        String owner,
        String address
) {
    public static BusinessLicenseInfoResponse from(BusinessLicenseOcrResponse businessLicenseOcrResponse) {
        Result result = businessLicenseOcrResponse.getResult();
        String cafeName = getBusinessLicenseInfo(result.companyName());
        String owner = getBusinessLicenseInfo(result.repName());
        String businessLicenseNumber = getBusinessLicenseInfo(result.registerNumber());
        String address = getBusinessLicenseInfo(result.bisAddress());
        return new BusinessLicenseInfoResponse(
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
