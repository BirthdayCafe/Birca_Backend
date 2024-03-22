package com.birca.bircabackend.command.like.presentation;

import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BirthdayCafeLikeControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 생일_카페를_찜한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/birthday-cafes/{birthdayCafeId}/like", birthdayCafeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("like-birthday-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("찜할 생일 카페 ID")
                        )
                ));
    }

    @Test
    void 생일_카페_찜을_취소한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;

        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/birthday-cafes/{birthdayCafeId}/like", birthdayCafeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("like-cancel-birthday-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("찜을 취소할 생일 카페 ID")
                        )
                ));
    }
}
