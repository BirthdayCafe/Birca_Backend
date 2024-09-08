package com.birca.bircabackend.command.birca.domain.value;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PhoneNumberTest {


    @Nested
    @DisplayName("연락처 생성 시")
    class FromTest {

        @ParameterizedTest
        @ValueSource(strings = {"010-1234-5678", "010-0000-0000", "010-2913-9583"})
        void 올바른_형식으로_생성한다(String number) {
            // when
            PhoneNumber phoneNumber = PhoneNumber.from(number);

            // then
            assertThat(phoneNumber.getValue()).isEqualTo(number);
        }

        @ParameterizedTest
        @ValueSource(strings = {
                "01012345678", "010-12345678", "0101234-5678",
                "01-1234-5678", "010-123-45678", "010-1234-678",
                "s01-1234-5678", "asd-asdf-qwer", "010-1234-a123"
        })
        void 형식이_올바르지_않으면_예외가_발생한다(String number) {
            // when then
            assertThatThrownBy(() -> PhoneNumber.from(number))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_PHONE_NUMBER);
        }
    }
}
