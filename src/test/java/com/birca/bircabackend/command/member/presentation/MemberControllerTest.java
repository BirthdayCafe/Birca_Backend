package com.birca.bircabackend.command.member.presentation;

import com.birca.bircabackend.command.member.dto.NicknameRegisterRequest;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
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
}
