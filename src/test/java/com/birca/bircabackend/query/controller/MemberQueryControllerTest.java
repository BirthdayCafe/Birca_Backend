package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.NicknameCheckResponse;
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
}
