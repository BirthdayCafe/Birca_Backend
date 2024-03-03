package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CafeQueryControllerTest extends DocumentationTest {

    @Test
    void 카페를_검색한다() throws Exception {
        // given
        given(cafeQueryService.findCafes(any()))
                .willReturn(List.of(
                        new CafeResponse(2L, "우지 커피"),
                        new CafeResponse(3L, "메가 커피")
                ));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/cafes/search")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("name", "커피")
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("search-cafes", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("name").description("카페 이름")
                        ),
                        responseFields(
                                fieldWithPath("[].cafeId").type(JsonFieldType.NUMBER).description("카페 ID"),
                                fieldWithPath("[].cafeName").type(JsonFieldType.STRING).description("카페 이름")
                        )
                ));
    }
}
