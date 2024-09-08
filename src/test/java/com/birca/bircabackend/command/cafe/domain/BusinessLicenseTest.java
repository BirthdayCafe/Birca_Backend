package com.birca.bircabackend.command.cafe.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessLicenseTest {

    @Nested
    @DisplayName("사업자등록증을 생성할 때")
    class CreateBusinessLicenseTest {

        @Test
        void 승인_여부는_false이다() {
            // when
            BusinessLicense businessLicense = BusinessLicense.createBusinessLicense(
                    1L, "최민혁",
                    "커피 벌스데이",
                    BusinessLicenseCode.from("123-45-67890"),
                    "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이",
                    "businessLicense.pdf"
            );

            // then
            assertThat(businessLicense.getRegistrationApproved()).isFalse();
        }
    }
}
