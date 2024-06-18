package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.domain.MemberRole;
import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.query.dto.ProfileResponse;
import com.birca.bircabackend.query.dto.RoleResponse;
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
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberQueryControllerTest extends DocumentationTest {


    @Test
    void 닉네임_중복을_검사한다() throws Exception {
        // given
        Long memberId = 1L;
        String requestNickname = "does";
        given(memberQueryService.checkNickname(requestNickname))
                .willReturn(new NicknameCheckResponse(true));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/join/check-nickname")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(memberId))
                .characterEncoding("UTF-8")
                .queryParam("nickname", requestNickname)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("check-nickname", HOST_INFO,
                        queryParameters(
                                parameterWithName("nickname").description("검사할 닉네임")
                        ),
                        responseFields(
                                fieldWithPath("success").type(JsonFieldType.BOOLEAN).description("중복 여부")
                        )
                ));
    }

    @Test
    void 자신의_프로필을_조회한다() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L);
        given(memberQueryService.getMyProfile(loginMember))
                .willReturn(new ProfileResponse("더즈"));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/members/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(loginMember.id()))
                .characterEncoding("UTF-8")
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-my-profile", HOST_INFO,
                        responseFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임")
                        )
                ));
    }

    @Test
    void 회원_역할을_조회한다() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L);
        given(memberQueryService.getMyRole(loginMember))
                .willReturn(new RoleResponse(MemberRole.HOST.toString()));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/members/role")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(loginMember.id()))
                .characterEncoding("UTF-8")
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-my-role", HOST_INFO,
                        responseFields(
                                fieldWithPath("role").type(JsonFieldType.STRING).description("역할")
                        )
                ));
    }
}
