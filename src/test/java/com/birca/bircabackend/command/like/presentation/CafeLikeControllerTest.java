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

class CafeLikeControllerTest extends DocumentationTest {

    private static final Long CAFE_ID = 1L;
    private static final Long MEMBER_ID = 1L;

    @Test
    void 카페를_찜한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(post("/api/v1/cafes/{cafeId}/like", CAFE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("like-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("cafeId").description("찜할 카페 ID")
                        )
                ));
    }

    @Test
    void 찜한_카페를_취소한다() throws Exception {
        // when
        ResultActions result = mockMvc.perform(delete("/api/v1/cafes/{cafeId}/like", CAFE_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect(status().isOk())
                .andDo(document("cancel-like-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("cafeId").description("찜 취소할 카페 ID")
                        )
                ));
    }
}
