package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.ArtistGroupResponse;
import com.birca.bircabackend.query.dto.ArtistResponse;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArtistQueryControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 아티스트_그룹_목록을_조회한다() throws Exception {
        // given
        given(artistGroupQueryService.findGroups(any()))
                .willReturn(List.of(
                        new ArtistGroupResponse(11L, "뉴진스", "newjeans.com"),
                        new ArtistGroupResponse(12L, "아이브", "ive.com"),
                        new ArtistGroupResponse(13L, "방탄소년단", "bts.com")
                ));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/artist-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("cursor", "10")
                .queryParam("size", "3")
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-artist-groups", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("cursor").description("이전에 쿼리된 마지막 groupId"),
                                parameterWithName("size").description("검색할 개수")
                        ),
                        responseFields(
                                fieldWithPath("[].groupId").type(JsonFieldType.NUMBER).description("아티스트 그룹 ID"),
                                fieldWithPath("[].groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름"),
                                fieldWithPath("[].groupImage").type(JsonFieldType.STRING).description("아티스트 그룹 이미지 url")
                        )
                ));
    }

    @Test
    void 아티스트_그룹의_멤버를_조회한다() throws Exception {
        // given
        Long groupId = 1L;
        given(artistQueryService.findArtistByGroup(groupId))
                .willReturn(List.of(
                        new ArtistResponse(5L, "뷔", "image5.com"),
                        new ArtistResponse(1L, "석진", "image1.com"),
                        new ArtistResponse(7L, "슈가", "image7.com"),
                        new ArtistResponse(2L, "정국", "image2.com"),
                        new ArtistResponse(3L, "제이홉", "image3.com"),
                        new ArtistResponse(6L, "지민", "image6.com"),
                        new ArtistResponse(4L, "RM", "image4.com")
                ));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/artist-groups/{groupId}/artists", groupId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-artist-group-members", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("groupId").description("조회할 그룹의 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].artistId").type(JsonFieldType.NUMBER).description("아티스트 ID"),
                                fieldWithPath("[].artistName").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("[].artistImage").type(JsonFieldType.STRING).description("아티스트 이미지 url")
                        )
                ));
    }

    @Test
    void 솔로_아티스트_목록을_조회한다() throws Exception {
        // given
        given(artistQueryService.findSoloArtists(any()))
                .willReturn(List.of(
                        new ArtistResponse(19L, "김범수", "image19.com"),
                        new ArtistResponse(14L, "로이킴", "image14.com"),
                        new ArtistResponse(15L, "박재정", "image15.com"),
                        new ArtistResponse(16L, "성시경", "image16.com"),
                        new ArtistResponse(13L, "아이유", "image13.com"),
                        new ArtistResponse(17L, "윤종신", "image17.com")
                ));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/artists/solo")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("cursor", "12")
                .queryParam("size", "6")
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-artists-solo", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("cursor").description("이전에 쿼리된 마지막 artistId"),
                                parameterWithName("size").description("검색할 개수")
                        ),
                        responseFields(
                                fieldWithPath("[].artistId").type(JsonFieldType.NUMBER).description("아티스트 ID"),
                                fieldWithPath("[].artistName").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("[].artistImage").type(JsonFieldType.STRING).description("아티스트 이미지 url")
                        )
                ));
    }
}
