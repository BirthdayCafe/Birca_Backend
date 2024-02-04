package com.birca.bircabackend.command.auth.presentation;

import com.birca.bircabackend.command.auth.dto.LoginRequest;
import com.birca.bircabackend.command.auth.exception.AuthErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends DocumentationTest {

    @Test
    void 소셜_로그인을_한() throws Exception {
        // given
        LoginRequest request = new LoginRequest("fake.kakao.accessToken");

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/oauth/login/{provider}", "kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("oauth-login",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        requestFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Oauth 리소스 서버의 엑세스토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("버카의 엑세스 토큰")
                        )
                ));
    }

    @Test
    void 인증_에러_코드() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/error-codes")
                .queryParam("className", "com.birca.bircabackend.command.auth.exception.AuthErrorCode")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("auth-error-Code",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        responseFields(getErrorDescriptor(AuthErrorCode.values()))
                ));
    }
}
