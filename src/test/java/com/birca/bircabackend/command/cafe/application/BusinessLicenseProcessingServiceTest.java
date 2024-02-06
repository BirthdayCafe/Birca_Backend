package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BusinessLicenseProcessingServiceTest extends ServiceTest {

    @Mock
    private OcrProvider ocrProvider;

    @InjectMocks
    private BusinessLicenseProcessingService businessLicenseProcessingService;

    @Test
    void 사업자등록증을_스캔한다() {
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
}