package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.*;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class BusinessLicenseCodeTest {

    @Nested
    @DisplayName("사업자등록증 번호 생성 시")
    class BusinessLicenseCodeCreateTest {

        @Test
        void 세무서_번호의_길이가_3이_아니면_예외가_발생한다() {
            //when then
            assertThatThrownBy(() -> BusinessLicenseCode.create(12,
                    34, 67890))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_TAX_OFFICE_CODE_LENGTH);
        }

        @Test
        void 사업자_성격_번호의_길이가_2가_아니면_예외가_발생한다() {
            //when then
            assertThatThrownBy(() -> BusinessLicenseCode.create(123,
                    4, 56789))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_BUSINESS_TYPE_CODE_LENGTH);
        }

        @Test
        void 일련_번호의_길이가_5가_아니면_예외가_발생한다() {
            //when then
            assertThatThrownBy(() -> BusinessLicenseCode.create(123,
                    45, 6789))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(INVALID_SERIAL_CODE_LENGTH);
        }
    }
}
