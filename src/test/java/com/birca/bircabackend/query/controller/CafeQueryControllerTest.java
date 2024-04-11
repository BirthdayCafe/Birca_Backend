package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.CafeDetailResponse;
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
}
