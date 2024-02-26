package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FavoriteArtistQueryControllerTest extends DocumentationTest {

    @Test
    void 최애_아티스트를_조회한다() throws Exception {
        // given
        given(favoriteArtistQueryService.findFavoriteArtist(any()))
                .willReturn(new ArtistResponse(10L, "민지", "image10.com"));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/artists/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-favorite-artist", HOST_INFO, DOCUMENT_RESPONSE,
                        responseFields(
                                fieldWithPath("artistId").type(JsonFieldType.NUMBER).description("최애 아티스트 ID"),
                                fieldWithPath("artistName").type(JsonFieldType.STRING).description("최애 아티스트 이름"),
                                fieldWithPath("artistImage").type(JsonFieldType.STRING).description("최애 아티스트 이미지 url")
                        )
                ));
    }
}
