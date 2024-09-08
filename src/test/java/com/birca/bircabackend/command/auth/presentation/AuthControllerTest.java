package com.birca.bircabackend.command.auth.presentation;

import com.birca.bircabackend.command.auth.dto.LoginRequest;
import com.birca.bircabackend.command.auth.dto.LoginResponse;
import com.birca.bircabackend.command.auth.exception.AuthErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends DocumentationTest {

    @Test
    void 소셜_로그인을_한다() throws Exception {
        // given
        LoginRequest request = new LoginRequest("fake.kakao.accessToken");
        String provider = "kakao";
        given(authFacade.login(request, provider))
                .willReturn(new LoginResponse(
                        "fake.birca.AccessToken", true, "VISITANT"
                ));

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/oauth/login/{provider}", provider)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("oauth-login",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("provider").description("OAuth를 제공하는 리소스 서버")
                        ),
                        requestFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("Oauth 리소스 서버의 엑세스 토큰")
                        ),
                        responseFields(
                                fieldWithPath("accessToken").type(JsonFieldType.STRING).description("버카의 엑세스 토큰"),
                                fieldWithPath("isNewMember").type(JsonFieldType.BOOLEAN).description("새로운 회원인지 여부"),
                                fieldWithPath("lastRole").type(JsonFieldType.STRING).description("마지막으로 활동한 역할")
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
