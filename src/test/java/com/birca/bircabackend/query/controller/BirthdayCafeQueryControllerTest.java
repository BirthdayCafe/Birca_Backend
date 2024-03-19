package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.LuckyDrawResponse;
import com.birca.bircabackend.query.dto.MenuResponse;
import com.birca.bircabackend.query.dto.MyBirthdayCafeResponse;
import com.birca.bircabackend.query.dto.SpecialGoodsResponse;
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
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BirthdayCafeQueryControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 생일_카페_특전_목록을_조회한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        given(birthdayCafeQueryService.findSpecialGoods(any()))
                .willReturn(List.of(
                        new SpecialGoodsResponse("특전", "포토카드"),
                        new SpecialGoodsResponse("디저트", "포토카드, ID 카드")
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes/{birthdayCafeId}/special-goods", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-special-goods", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("생일 카페 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("굿즈명"),
                                fieldWithPath("[].details").type(JsonFieldType.STRING).description("굿즈 구성품")
                        )
                ));
    }

    @Test
    void 생일_카페_메뉴_목록을_조회한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        given(birthdayCafeQueryService.findMenus(any()))
                .willReturn(List.of(
                        new MenuResponse("기본", "아메리카노+포토카드+ID카드", 6000),
                        new MenuResponse("디저트", "케이크+포토카드+ID카드", 10000)
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes/{birthdayCafeId}/menus", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-menus", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("생일 카페 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("메뉴명"),
                                fieldWithPath("[].details").type(JsonFieldType.STRING).description("메뉴 구성"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("메뉴 가격")
                        )
                ));
    }

    @Test
    void 생일_카페_럭키_드로우_목록을_조회한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        given(birthdayCafeQueryService.findLuckyDraws(any()))
                .willReturn(List.of(
                        new LuckyDrawResponse(1, "티셔츠"),
                        new LuckyDrawResponse(2, "머그컵")
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes/{birthdayCafeId}/lucky-draws", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-lucky-draws", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("생일 카페 ID")
                        ),
                        responseFields(
                                fieldWithPath("[].rank").type(JsonFieldType.NUMBER).description("등수"),
                                fieldWithPath("[].prize").type(JsonFieldType.STRING).description("상품")
                        )
                ));
    }

    @Test
    void 주최자의_생일_카페_목록을_조회한다() throws Exception {
        // given
        given(birthdayCafeQueryService.findMyBirthdayCafes(new LoginMember(MEMBER_ID)))
                .willReturn(List.of(
                        new MyBirthdayCafeResponse(
                                1L,
                                "image.com",
                                LocalDateTime.of(2024, 3, 18, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 19, 0, 0, 0),
                                "민호의 생일 카페",
                                "FINISHED",
                                new MyBirthdayCafeResponse.ArtistResponse("샤이니", "민호")
                        ),
                        new MyBirthdayCafeResponse(
                                2L,
                                "image.com",
                                LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 23, 0, 0, 0),
                                "아이유의 생일 카페",
                                "IN_PROGRESS",
                                new MyBirthdayCafeResponse.ArtistResponse(null, "아이유")
                        )
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes/me")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-my-birthday-cafe-list", HOST_INFO, DOCUMENT_RESPONSE,
                        responseFields(
                                fieldWithPath("[].birthdayCafeId").type(JsonFieldType.NUMBER).description("생일 카페 ID"),
                                fieldWithPath("[].mainImageUrl").type(JsonFieldType.STRING).description("생일 카페 메인 이미지 url"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("생일 카페 종료일"),
                                fieldWithPath("[].birthdayCafeName").type(JsonFieldType.STRING).description("생일 카페 이름"),
                                fieldWithPath("[].progressState").type(JsonFieldType.STRING).description("생일 카페 진행 상태"),
                                fieldWithPath("[].artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("[].artist.name").type(JsonFieldType.STRING).description("아티스트 이름")
                        )
                ));
    }
}
