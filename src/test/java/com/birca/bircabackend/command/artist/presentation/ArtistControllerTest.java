package com.birca.bircabackend.command.artist.presentation;

import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ArtistControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 최애_아티스트를_등록_한다() throws Exception {
        // given
        long artistId = 8L;
        FavoriteArtistRequest request = new FavoriteArtistRequest(artistId);

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/artists/favorite")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("register-favorite-artist", HOST_INFO, DOCUMENT_RESPONSE,
                        requestFields(
                                fieldWithPath("artistId").type(JsonFieldType.NUMBER).description("최애 아티스트 ID")
                        )
                ));
    }
}
