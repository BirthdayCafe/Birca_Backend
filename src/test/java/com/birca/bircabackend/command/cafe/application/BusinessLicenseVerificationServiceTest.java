package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class BusinessLicenseVerificationServiceTest extends ServiceTest {

    @Mock
    private VerificationProvider verificationProvider;

    @InjectMocks
    private BusinessLicenseVerificationService businessLicenseVerificationService;

    @Nested
    @DisplayName("사업자등록증을")
    class BusinessLicenseVerificationTest {

        @Test
        void 검증할_때_등록된_사업자등록번호는_정상_처리된다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            // when
            businessLicenseVerificationService.verifyBusinessLicenseStatus(businessLicenseNumber);

            // then
            verify(verificationProvider).verifyBusinessLicenseStatus(businessLicenseNumber);
        }

        @Test
        void 검증할_때_등록되지_않은_사업자등록번호는_예외가_발생한다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            doThrow(BusinessException.from(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER))
                    .when(verificationProvider)
                    .verifyBusinessLicenseStatus(businessLicenseNumber);

            // when then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> businessLicenseVerificationService.verifyBusinessLicenseStatus(businessLicenseNumber));
            assertThat(exception.getErrorCode()).isEqualTo(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER);
        }
    }
}