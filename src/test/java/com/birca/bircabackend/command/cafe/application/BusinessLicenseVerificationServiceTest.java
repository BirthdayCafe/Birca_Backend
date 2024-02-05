package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseVerificationApi;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

class BusinessLicenseVerificationServiceTest extends ServiceTest {

    @Mock
    private BusinessLicenseVerificationApi verificationApi;

    @InjectMocks
    private BusinessLicenseVerificationService businessLicenseVerificationService;

    @Nested
    @DisplayName("사업자등록증을")
    class BusinessLicenseOVerificationTest {

        @Test
        void 검증할_때_등록된_사업자등록번호는_정상_처리된다() {
            // given
            String businessLicenseNumber = "123-45-67890";
            BusinessLicenseStatusResponse businessLicenseStatusResponse = new BusinessLicenseStatusResponse(
                    "example_status_code",
                    List.of(new BusinessLicenseStatusResponse.Data("국체청에 등록된 사업자등록번호입니다."))
            );

            when(verificationApi.verifyBusinessLicenseStatus(any()))
                    .thenReturn(businessLicenseStatusResponse);

            // when
            businessLicenseVerificationService.verifyBusinessLicenseStatus(businessLicenseNumber);

            // then
            verify(verificationApi, times(1)).verifyBusinessLicenseStatus(any());
        }

        @Test
        void 검증할_때_등록되지_않은_사업자등록번호는_예외가_발생한다() {
            // given
            String businessLicenseNumber = "123-45-67890";
            BusinessLicenseStatusResponse businessLicenseStatusResponse = new BusinessLicenseStatusResponse(
                    "example_status_code",
                    List.of(new BusinessLicenseStatusResponse.Data("국세청에 등록되지 않은 사업자등록번호입니다."))
            );

            when(verificationApi.verifyBusinessLicenseStatus(any()))
                    .thenReturn(businessLicenseStatusResponse);

            // when then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> businessLicenseVerificationService.verifyBusinessLicenseStatus(businessLicenseNumber));
            assertThat(exception.getErrorCode()).isEqualTo(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER);
        }
    }
}