package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.BusinessLicenseInfo;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.Result;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseOcrClient;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseVerificationClient;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

class BusinessLicenseProcessingServiceTest extends ServiceTest {

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
            MockMultipartFile businessLicense = new MockMultipartFile("businessLicense",
                    "businessLicense.pdf", "application/pdf", "businessLicense".getBytes(UTF_8));
            Result mockResult = new Result(
                    List.of(new BusinessLicenseInfo("address")),
                    List.of(new BusinessLicenseInfo("123-45-67890")),
                    List.of(new BusinessLicenseInfo("cafeName")),
                    List.of(new BusinessLicenseInfo("owner"))
            );

            BizLicense mockBizLicense = new BizLicense(mockResult);
            Image mockImage = new Image(mockBizLicense);
            BusinessLicenseOcrResponse mockOcrResponse = new BusinessLicenseOcrResponse(List.of(mockImage));

            given(businessLicenseProcessingService.readBusinessLicense(businessLicense))
                    .willReturn(BusinessLicenseResponse.from(mockOcrResponse));

            //when
            BusinessLicenseResponse response = businessLicenseProcessingService.readBusinessLicense(businessLicense);

            //then
            assertEquals("address", response.address());
            assertEquals("123-45-67890", response.businessLicenseNumber());
            assertEquals("cafeName", response.cafeName());
            assertEquals("owner", response.owner());
        }

        @Test
        void 검증한다() {
            // given
            String businessLicenseNumber = "123-45-67890";

            // when
            businessLicenseProcessingService.verifyBusinessLicenseStatus(businessLicenseNumber);

            // then
            verify(businessLicenseVerificationClient).verifyBusinessLicenseStatus(businessLicenseNumber);
        }
    }
}