package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BusinessLicenseQueryControllerTest extends DocumentationTest {

    @Test
    void 사업자등록증_승인_여부를_조회한다() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L);
        given(businessLicenseQueryService.getBusinessLicenseStatus(loginMember))
                .willReturn(new BusinessLicenseStatusResponse(true));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/business-license/status")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(loginMember.id()))
                .characterEncoding("UTF-8")
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-business-license-status", HOST_INFO,
                        responseFields(
                                fieldWithPath("registrationApproved").type(JsonFieldType.BOOLEAN).description("사업자등록증 승인 여부")
                        )
                ));
    }
}