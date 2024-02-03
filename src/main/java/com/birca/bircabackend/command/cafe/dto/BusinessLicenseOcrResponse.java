package com.birca.bircabackend.command.cafe.dto;

import java.util.List;

public record BusinessLicenseOcrResponse(
        List<Image> images
) {
    public record Image(
            BizLicense bizLicense
    ) {
        public record BizLicense(
                Result result
        ) {

            public record Result(
                    List<BusinessLicenseInfo> bisAddress,
                    List<BusinessLicenseInfo> registerNumber,
                    List<BusinessLicenseInfo> companyName,
                    List<BusinessLicenseInfo> repName
            ) {
            }

            public record BusinessLicenseInfo(
                    String text
            ) {
            }
        }
    }
}
