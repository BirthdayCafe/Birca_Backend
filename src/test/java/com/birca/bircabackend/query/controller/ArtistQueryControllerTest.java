package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.ArtistGroupResponse;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ArtistQueryControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 아티스트_그룹_목록을_조회한다() throws Exception {
        // given
        BDDMockito.given(artistGroupQueryService.findGroups(any()))
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
}
