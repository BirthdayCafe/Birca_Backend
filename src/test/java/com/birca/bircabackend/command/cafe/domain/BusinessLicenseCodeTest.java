package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.INVALID_BUSINESS_LICENSE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessLicenseCodeTest {

    @Nested
    @DisplayName("사업자등록증 번호 생성 시")
    class BusinessLicenseCodeCreateTest {

        @ParameterizedTest
        @ValueSource(strings = {"012-34-56789"})
        void 올바른_형식으로_생성한다(String businessLicense) {
            // when
            BusinessLicenseCode businessLicenseCode = BusinessLicenseCode.from(businessLicense);

            // then
            assertThat(businessLicenseCode.getTaxOfficeCode()).isEqualTo("012");
            assertThat(businessLicenseCode.getBusinessTypeCode()).isEqualTo("34");
            assertThat(businessLicenseCode.getSerialCode()).isEqualTo("56789");
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "0123456789", "01-12-34567", "012-3-45678", "012-23-4567"
        })
        void 형식에_맞지_않은_사업자등록번호는_예외가_발생한다(String businessLicense) {
            //when then
            assertThatThrownBy(() -> BusinessLicenseCode.from(businessLicense))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_BUSINESS_LICENSE_NUMBER);
        }
    }
}
