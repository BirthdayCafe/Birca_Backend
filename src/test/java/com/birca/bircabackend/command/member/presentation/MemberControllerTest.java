package com.birca.bircabackend.command.member.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.dto.NicknameRegisterRequest;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 회원의_역할을_변경한다() throws Exception {
        // given
        RoleChangeRequest request = new RoleChangeRequest("HOST");

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/members/role-change")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("member-role-change", HOST_INFO));
    }

    @Test
    void 회원의_닉네임을_등록한다() throws Exception {
        // given
        NicknameRegisterRequest request = new NicknameRegisterRequest("닉네임");

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/join/register-nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("member-nickname-register", HOST_INFO,
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("등록할 닉네임")
                        )
                ));
    }

    @Test
    void 회원을_탈퇴한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/api/v1/members/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("member-withdraw", HOST_INFO));
    }

    @Test
    void 회원_에러_코드() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/error-codes")
                .queryParam("className", "com.birca.bircabackend.command.member.exception.MemberErrorCode")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("member-error-Code",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        responseFields(getErrorDescriptor(MemberErrorCode.values()))
                ));
    }
}
