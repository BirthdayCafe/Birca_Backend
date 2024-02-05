package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.BusinessLicenseInfo;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseOcrResponse.Image.BizLicense.Result;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseStatusResponse.Data;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseOcrApi;
import com.birca.bircabackend.command.cafe.infrastructure.BusinessLicenseVerificationApi;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.NOT_REGISTERED_BUSINESS_LICENSE_NUMBER;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

class BusinessLicenseOcrServiceTest extends ServiceTest {

    @Mock
    private BusinessLicenseOcrApi ocrApi;

    @InjectMocks
    private BusinessLicenseOcrService businessLicenseOcrService;

    @Nested
    @DisplayName("사업자등록증을")
    class BusinessLicenseOcrTest {

        @Test
        void 스캔한다() {
            // given
            MockMultipartFile businessLicense = new MockMultipartFile("businessLicense",
                    "businessLicense.pdf", "application/pdf", "businessLicense".getBytes(UTF_8));
            Result result = new Result(
                    List.of(new BusinessLicenseInfo("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이")),
                    List.of(new BusinessLicenseInfo("123-45-67890")),
                    List.of(new BusinessLicenseInfo("카페 벌스데이")),
                    List.of(new BusinessLicenseInfo("최민혁"))
            );

            BizLicense bizLicense = new BizLicense(result);
            Image image = new Image(bizLicense);
            BusinessLicenseOcrResponse ocrResponse = new BusinessLicenseOcrResponse(List.of(image));

            given(ocrApi.performBusinessLicenseOcr(any(), any()))
                    .willReturn(ocrResponse);

            //when
            BusinessLicenseResponse response =
                    businessLicenseOcrService.readBusinessLicense(businessLicense);

            // then
            assertEquals("서울 마포구 와우산로29길 26-33 1층 커피 벌스데이", response.address());
            assertEquals("123-45-67890", response.businessLicenseNumber());
            assertEquals("카페 벌스데이", response.cafeName());
            assertEquals("최민혁", response.owner());
        }
    }
}