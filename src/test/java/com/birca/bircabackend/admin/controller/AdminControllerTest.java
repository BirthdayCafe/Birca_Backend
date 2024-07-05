package com.birca.bircabackend.admin.controller;

import com.birca.bircabackend.admin.dto.AdminAuthRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 사업자등록증을_승인한다() throws Exception {
        // given
        Long businessLicenseId = 1L;
        AdminAuthRequest request = new AdminAuthRequest("id", "password");

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/approve/{businessLicenseId}", businessLicenseId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("approve-business-license", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(parameterWithName("businessLicenseId").description("사업자등록증 ID")),
                        requestFields(
                                fieldWithPath("id").type(JsonFieldType.STRING).description("관리자 아이디"),
                                fieldWithPath("password").type(JsonFieldType.STRING).description("관리자 비밀번호")
                        )
                ));
    }
}