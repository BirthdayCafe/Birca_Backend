package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BusinessLicenseControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 사업자등록증을_스캔한다() throws Exception {
        // given
        MockMultipartFile businessLicenseFile = new MockMultipartFile(
                "businessLicense", "businessLicense.pdf",
                "application/pdf", "businessLicense.pdf".getBytes());

        BusinessLicenseResponse mockResponse = new BusinessLicenseResponse(
                "Cafe Name",
                "123-45-67890",
                "Owner",
                "Address"
        );

        when(businessLicenseProcessingService.getBusinessLicenseInfo(businessLicenseFile))
                .thenReturn(mockResponse);

        // when
        ResultActions result = mockMvc.perform(multipart("/api/v1/cafes/license-read")
                .file(businessLicenseFile)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("business-license-read",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        requestParts(
                                partWithName("businessLicense").description("사업자등록증")
                        ),
                        responseFields(
                                fieldWithPath("cafeName").type(JsonFieldType.STRING).description("카페 이름"),
                                fieldWithPath("businessLicenseNumber").type(JsonFieldType.STRING).description("사업자등록증번호"),
                                fieldWithPath("owner").type(JsonFieldType.STRING).description("카페 사장"),
                                fieldWithPath("address").type(JsonFieldType.STRING).description("카페 주소")
                        )
                ));
    }

    //에러
    @Test
    void 사업자등록증을_저장한다() throws Exception {
        // given
        MockMultipartFile businessLicenseFile = new MockMultipartFile(
                "businessLicense", "test.pdf", "application/pdf",
                "businessLicense.pdf".getBytes());

        // when
        ResultActions result = mockMvc.perform(multipart("/api/v1/cafes/apply")
                .file(businessLicenseFile)
                .param("cafeName", "커피 벌스데이")
                .param("businessLicenseNumber", "123-45-67890")
                .param("owner", "최민혁")
                .param("address", "서울 마포구 와우산로29길 26-33 1층 커피 벌스데이")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("business-license-save", HOST_INFO,
                        requestParts(
                                partWithName("businessLicense").description("사업자등록증")
                        )
                ));
    }

    @Test
    void 사업자등록증_에러_코드() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/error-codes")
                .queryParam("className", "com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("business-license-error-Code",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        responseFields(getErrorDescriptor(BusinessLicenseErrorCode.values()))
                ));
    }
}
