package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
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

    @Test
    void 사장님이_카페_상세_조회를_한다() throws Exception {
        // given
        given(cafeQueryService.findCafeDetail(new LoginMember(1L)))
                .willReturn(
                        new CafeDetailResponse(
                                "메가커피",
                                "서울특별시 강남구 테헤란로 212",
                                "@ChaseM",
                                "8시 - 22시",
                                List.of(new CafeDetailResponse.CafeMenuResponse("아메리카노", 1500)),
                                List.of(new CafeDetailResponse.CafeOptionResponse("액자", 2000)),
                                List.of("image.com")
                        )
                );

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/cafes/me")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-cafe-detail", HOST_INFO, DOCUMENT_RESPONSE,
                        responseFields(
                                fieldWithPath("cafeName").type(JsonFieldType.STRING).description("카페 이름"),
                                fieldWithPath("cafeAddress").type(JsonFieldType.STRING).description("카페 주소"),
                                fieldWithPath("twitterAccount").type(JsonFieldType.STRING).description("트위터 계정"),
                                fieldWithPath("businessHours").type(JsonFieldType.STRING).description("영업 시간"),
                                fieldWithPath("cafeMenus").type(JsonFieldType.ARRAY).description("카페 메뉴 리스트"),
                                fieldWithPath("cafeMenus[].name").type(JsonFieldType.STRING).description("메뉴명"),
                                fieldWithPath("cafeMenus[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("cafeOptions").type(JsonFieldType.ARRAY).description("카페 데코레이션 및 추가서비스 리스트"),
                                fieldWithPath("cafeOptions[].name").type(JsonFieldType.STRING).description("옵션명"),
                                fieldWithPath("cafeOptions[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("cafeImages").type(JsonFieldType.ARRAY).description("카페 이미지")
                        )
                ));
    }

    @Test
    void 대관_가능한_카페_목록을_조회한다() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L);

        CafeParams cafeParams = new CafeParams();
        LocalDateTime startDate = LocalDateTime.of(2024, 3, 18, 0, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2024, 3, 19, 0, 0, 0);
        Boolean liked = true;
        cafeParams.setStartDate(startDate);
        cafeParams.setEndDate(endDate);
        cafeParams.setLiked(liked);

        PagingParams pagingParams = new PagingParams();
        long cursor = 1L;
        int size = 10;
        pagingParams.setCursor(cursor);
        pagingParams.setSize(size);
        given(cafeQueryService.searchCafes(loginMember, cafeParams, pagingParams))
                .willReturn(List.of(
                        new CafeSearchResponse(
                                1L,
                                true,
                                "image1.com",
                                "@ChaseM",
                                "서울특별시 강남구 테헤란로 212"
                        ),
                        new CafeSearchResponse(
                                2L,
                                true,
                                "image2.com",
                                "@ChaseM",
                                "경기도 성남시 분당구 판교역로 235"
                        )
                ));

        // when
        ResultActions result = mockMvc.perform(get("/api/v1/cafes")
                .contentType(MediaType.APPLICATION_JSON)
                .queryParam("startDate", String.valueOf(startDate))
                .queryParam("endDate", String.valueOf(endDate))
                .queryParam("liked", String.valueOf(liked))
                .queryParam("cursor", String.valueOf(cursor))
                .queryParam("size", String.valueOf(size))
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("search-rental-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("startDate").description("검색 시작 날짜"),
                                parameterWithName("endDate").description("검색 마지막 날짜"),
                                parameterWithName("liked").description("찜한 카페 목록 검색 여부"),
                                parameterWithName("cursor").description("이전에 쿼리된 마지막 cafeId"),
                                parameterWithName("size").description("검색할 개수")
                        ),
                        responseFields(
                                fieldWithPath("[].cafeId").type(JsonFieldType.NUMBER).description("카페 이름"),
                                fieldWithPath("[].liked").type(JsonFieldType.BOOLEAN).description("카페 찜 여부"),
                                fieldWithPath("[].cafeImageUrl").type(JsonFieldType.STRING).description("카페 이미지"),
                                fieldWithPath("[].twitterAccount").type(JsonFieldType.STRING).description("트위터 계정"),
                                fieldWithPath("[].address").type(JsonFieldType.STRING).description("카페 주소")
                        )
                ));
    }
}
