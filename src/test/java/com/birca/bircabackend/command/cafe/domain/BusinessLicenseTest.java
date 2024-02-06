package com.birca.bircabackend.command.cafe.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessLicenseTest {

    @Test
    void 사업자등록증을_정상적으로_생성한다() {
        // when
        BusinessLicense businessLicense = BusinessLicense.createBusinessLicense(
                1L, "최민혁",
                "커피 벌스데이",
                BusinessLicenseCode.createBusinessLicenseCode(123, 45, 67890),
                "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이",
                "businessLicense.pdf"
        );

        // then
        assertThat(businessLicense.getCafeName()).isEqualTo("커피 벌스데이");
        assertThat(businessLicense.getOwnerName()).isEqualTo("최민혁");
        assertThat(businessLicense.getAddress()).isEqualTo("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이");
        assertThat(businessLicense.getCode().getTaxOfficeCode()).isEqualTo(123);
        assertThat(businessLicense.getCode().getBusinessTypeCode()).isEqualTo(45);
        assertThat(businessLicense.getCode().getSerialCode()).isEqualTo(67890);
    }
}
