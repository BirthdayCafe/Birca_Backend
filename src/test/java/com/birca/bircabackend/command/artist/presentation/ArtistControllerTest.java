package com.birca.bircabackend.command.artist.presentation;

import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.artist.dto.InterestArtistRequest;
import com.birca.bircabackend.command.artist.exception.ArtistErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

    @Test
    void 관심_아티스트를_등록_한다() throws Exception {
        // given
        List<InterestArtistRequest> request = List.of(
                new InterestArtistRequest(1L),
                new InterestArtistRequest(2L),
                new InterestArtistRequest(3L)
        );

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/artists/interest")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("register-interest-artist", HOST_INFO, DOCUMENT_RESPONSE,
                        requestFields(
                                fieldWithPath("[].artistId").type(JsonFieldType.NUMBER).description("관심 아티스트 ID")
                        )
                ));
    }

    @Test
    void 아티스트_에러_코드() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/error-codes")
                .queryParam("className", "com.birca.bircabackend.command.artist.exception.ArtistErrorCode")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("artist-error-Code",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        responseFields(getErrorDescriptor(ArtistErrorCode.values()))
                ));
    }
}
