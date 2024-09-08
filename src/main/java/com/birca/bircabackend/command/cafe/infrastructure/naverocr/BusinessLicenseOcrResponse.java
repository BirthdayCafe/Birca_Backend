package com.birca.bircabackend.command.cafe.infrastructure.naverocr;

import com.birca.bircabackend.command.cafe.infrastructure.naverocr.BusinessLicenseOcrResponse.Image.BizLicense.Result;

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

    public Result getResult() {
        return this.images().get(0).bizLicense().result();
    }
}
