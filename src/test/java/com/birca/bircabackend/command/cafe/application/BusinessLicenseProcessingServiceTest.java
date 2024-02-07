package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class BusinessLicenseProcessingServiceTest extends ServiceTest {

    @Mock
    private OcrProvider ocrProvider;

    @Mock
    private BusinessLicenseStatusVerifier businessLicenseStatusVerifier;

    @InjectMocks
    private BusinessLicenseProcessingService businessLicenseProcessingService;

    @Nested
    @DisplayName("사업자등록증을")
    class BusinessLicenseProcessingTest {

        @Test
        void 스캔한다() {
            // given
            MockMultipartFile businessLicense = new MockMultipartFile(
                    "businessLicense", "test.pdf", "application/pdf", "Business License Content".getBytes());

            BusinessLicenseResponse expectedResponse = new BusinessLicenseResponse(
                    "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이",
                    "123-45-67890",
                    "최민혁",
                    "카페 벌스데이"
            );

            when(ocrProvider.getBusinessLicenseInfo(businessLicense)).thenReturn(expectedResponse);

            // when
            BusinessLicenseResponse actualResponse = businessLicenseProcessingService.getBusinessLicenseInfo(businessLicense);

            // then
            verify(ocrProvider).getBusinessLicenseInfo(businessLicense);
            assertEquals(expectedResponse, actualResponse);
        }

        @Test
        void 검증할_때_등록된_사업자등록번호는_정상_처리된다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            // when
            businessLicenseProcessingService.verifyBusinessLicenseStatus(businessLicenseNumber);

            // then
            verify(businessLicenseStatusVerifier).verifyBusinessLicenseStatus(businessLicenseNumber);
        }

        @Test
        void 검증할_때_등록되지_않은_사업자등록번호는_예외가_발생한다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            doThrow(BusinessException.from(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER))
                    .when(businessLicenseStatusVerifier)
                    .verifyBusinessLicenseStatus(businessLicenseNumber);

            // when then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> businessLicenseProcessingService.verifyBusinessLicenseStatus(businessLicenseNumber));
            assertThat(exception.getErrorCode()).isEqualTo(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER);
        }
    }
}
