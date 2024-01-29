package com.birca.bircabackend.command.member.presentation;

import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemberControllerTest extends DocumentationTest {

    @Test
    void 회원의_역할을_변경한다() throws Exception {
        // given
        Long memberId = 1L;
        RoleChangeRequest request = new RoleChangeRequest("HOST");

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/members/role-change")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(memberId))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("member-role-change", HOST_INFO));
    }
}
