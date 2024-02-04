package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.BusinessLicenseInfo;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.Result;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseOcrClient;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseVerificationClient;
import com.birca.bircabackend.command.cafe.infrastructure.OcrRequestCounter;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_BUSINESS_LICENSE_OR_IMAGE_PROCESSING_FAILED;
import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

class BusinessLicenseProcessingServiceTest extends ServiceTest {

    @Mock
    private OcrRequestCounter ocrRequestCounter;

    @Mock
    private BusinessLicenseOcrClient businessLicenseOcrClient;

    @Mock
    private BusinessLicenseVerificationClient businessLicenseVerificationClient;

    @InjectMocks
    private BusinessLicenseProcessingService businessLicenseProcessingService;

    @Nested
    @DisplayName("사업자등록증을")
    class BusinessLicenseProcessingTest {

        @Test
        void 스캔한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);
            MockMultipartFile businessLicense = new MockMultipartFile("businessLicense",
                    "businessLicense.pdf", "application/pdf", "businessLicense".getBytes(UTF_8));
            Result mockResult = new Result(
                    List.of(new BusinessLicenseInfo("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이")),
                    List.of(new BusinessLicenseInfo("123-45-67890")),
                    List.of(new BusinessLicenseInfo("카페 벌스데이")),
                    List.of(new BusinessLicenseInfo("최민혁"))
            );

            BizLicense mockBizLicense = new BizLicense(mockResult);
            Image mockImage = new Image(mockBizLicense);
            BusinessLicenseOcrResponse mockOcrResponse = new BusinessLicenseOcrResponse(List.of(mockImage));

            given(businessLicenseProcessingService.readBusinessLicense(loginMember, businessLicense))
                    .willReturn(BusinessLicenseResponse.from(mockOcrResponse));

            //when
            BusinessLicenseResponse response =
                    businessLicenseProcessingService.readBusinessLicense(loginMember, businessLicense);

            //then
            assertEquals("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이", response.address());
            assertEquals("123-45-67890", response.businessLicenseNumber());
            assertEquals("카페 벌스데이", response.cafeName());
            assertEquals("최민혁", response.owner());
        }

        @Test
        void 스캔할_때_사업자등록증이_아니거나_이미지_처리에_실패하면_에러가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);
            MockMultipartFile businessLicense = new MockMultipartFile("businessLicense",
                    "businessLicense.pdf", "application/pdf", "businessLicense".getBytes(UTF_8));

            given(businessLicenseProcessingService.readBusinessLicense(loginMember, businessLicense))
                    .willThrow(BusinessException.from(NOT_BUSINESS_LICENSE_OR_IMAGE_PROCESSING_FAILED));

            // when then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> businessLicenseProcessingService.readBusinessLicense(loginMember, businessLicense));
            assertThat(exception.getErrorCode()).isEqualTo(NOT_BUSINESS_LICENSE_OR_IMAGE_PROCESSING_FAILED);
        }


        @Test
        void 검증할_때_등록된_사업자등록번호는_정상_처리된다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            // when
            businessLicenseProcessingService.verifyBusinessLicenseStatus(businessLicenseNumber);

            // then
            verify(businessLicenseVerificationClient).verifyBusinessLicenseStatus(businessLicenseNumber);
        }

        @Test
        void 검증할_때_등록되지_않은_사업자등록번호는_예외가_발생한다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            doThrow(BusinessException.from(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER))
                    .when(businessLicenseVerificationClient)
                    .verifyBusinessLicenseStatus(businessLicenseNumber);

            // when then
            BusinessException exception = assertThrows(BusinessException.class,
                    () -> businessLicenseProcessingService.verifyBusinessLicenseStatus(businessLicenseNumber));
            assertThat(exception.getErrorCode()).isEqualTo(NOT_REGISTERED_BUSINESS_LICENSE_NUMBER);
            verify(businessLicenseVerificationClient).verifyBusinessLicenseStatus(businessLicenseNumber);
        }
    }
}