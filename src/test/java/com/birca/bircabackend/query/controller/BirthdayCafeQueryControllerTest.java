package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.value.CongestionState;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.domain.value.SpecialGoodsStockState;
import com.birca.bircabackend.command.birca.domain.value.Visibility;
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
import static org.springframework.restdocs.request.RequestDocumentation.*;
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
                .andDo(document("get-birthday-cafe-menus", HOST_INFO, DOCUMENT_RESPONSE,
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

    @Test
    void 방문자가_생일_카페_목록을_조회한다() throws Exception {
        // given
        PagingParams pagingParams = new PagingParams();
        long cursor = 1L;
        int size = 10;
        String name = "민호";
        pagingParams.setCursor(cursor);
        pagingParams.setSize(size);

        BirthdayCafeParams birthdayCafeParams = new BirthdayCafeParams();
        String progressState = "IN_PROGRESS";
        birthdayCafeParams.setName(name);
        birthdayCafeParams.setProgressState(progressState);
        given(birthdayCafeQueryService.findBirthdayCafes(birthdayCafeParams, pagingParams, new LoginMember(1L)))
                .willReturn(List.of(
                        new BirthdayCafeResponse(
                                1L,
                                "image.com",
                                LocalDateTime.of(2024, 3, 18, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 19, 0, 0, 0),
                                "민호의 생일 카페",
                                true,
                                new BirthdayCafeResponse.ArtistResponse("샤이니", "민호"),
                                new BirthdayCafeResponse.CafeResponse("경기도 성남시 분당구 판교역로 235")
                        ),
                        new BirthdayCafeResponse(
                                2L,
                                "image.com",
                                LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 23, 0, 0, 0),
                                "아이유의 생일 카페",
                                false,
                                new BirthdayCafeResponse.ArtistResponse(null, "아이유"),
                                new BirthdayCafeResponse.CafeResponse("서울특별시 강남구 테헤란로 212")
                        )
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("cursor", String.valueOf(cursor))
                        .queryParam("size", String.valueOf(size))
                        .queryParam("name", name)
                        .queryParam("progressState", progressState)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-list", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("cursor").description("이전에 쿼리된 마지막 birthdayCafeId"),
                                parameterWithName("size").description("검색할 개수"),
                                parameterWithName("name").description("카페 이름 혹은 생일 카페의 아티스트 이름"),
                                parameterWithName("progressState").description("생일 카페 진행 상태")
                        ),
                        responseFields(
                                fieldWithPath("[].birthdayCafeId").type(JsonFieldType.NUMBER).description("생일 카페 ID"),
                                fieldWithPath("[].mainImageUrl").type(JsonFieldType.STRING).description("생일 카페 메인 이미지 url"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("생일 카페 종료일"),
                                fieldWithPath("[].birthdayCafeName").type(JsonFieldType.STRING).description("생일 카페 이름"),
                                fieldWithPath("[].isLiked").type(JsonFieldType.BOOLEAN).description("찜 했는지 여부"),
                                fieldWithPath("[].artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("[].artist.name").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("[].cafe.address").type(JsonFieldType.STRING).description("카페 주소")
                        )
                ));
    }

    @Test
    void 생일_카페_상세를_조회한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        given(birthdayCafeQueryService.findBirthdayCafeDetail(any(), any()))
                .willReturn(new BirthdayCafeDetailResponse(
                        new BirthdayCafeDetailResponse.CafeResponse(
                                "스타벅스", "경기도 성남시 분당구 판교역로 235", List.of("image1.com", "image2.com")
                        ),
                        new BirthdayCafeDetailResponse.ArtistResponse("샤이니", "민호"),
                        LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                        LocalDateTime.of(2024, 3, 23, 0, 0, 0),
                        "아이유의 생일 카페",
                        10,
                        true,
                        "@ChaseM",
                        5,
                        10,
                        CongestionState.SMOOTH,
                        Visibility.PUBLIC,
                        ProgressState.IN_PROGRESS,
                        SpecialGoodsStockState.ABUNDANT,
                        "image.com",
                        List.of("image1.com", "image2.com")
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes/{birthdayCafeId}", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-detail", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("생일 카페 ID")
                        ),
                        responseFields(
                                fieldWithPath("cafe.name").type(JsonFieldType.STRING).description("카페 이름"),
                                fieldWithPath("cafe.address").type(JsonFieldType.STRING).description("카페 주소"),
                                fieldWithPath("cafe.images").type(JsonFieldType.ARRAY).description("카페 사진"),
                                fieldWithPath("artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("artist.name").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("생일 카페 종료일"),
                                fieldWithPath("birthdayCafeName").type(JsonFieldType.STRING).description("생일 카페 이름"),
                                fieldWithPath("likeCount").type(JsonFieldType.NUMBER).description("생일 카페 찜된 수"),
                                fieldWithPath("isLiked").type(JsonFieldType.BOOLEAN).description("찜 했는지 여부"),
                                fieldWithPath("twitterAccount").type(JsonFieldType.STRING).description("트위터 계정"),
                                fieldWithPath("minimumVisitant").type(JsonFieldType.NUMBER).description("생일 카페 최소 방문자 수"),
                                fieldWithPath("maximumVisitant").type(JsonFieldType.NUMBER).description("생일 카페 최대 방문자 수"),
                                fieldWithPath("congestionState").type(JsonFieldType.STRING).description("생일 카페 혼잡도"),
                                fieldWithPath("visibility").type(JsonFieldType.STRING).description("생일 카페 공개 여부"),
                                fieldWithPath("progressState").type(JsonFieldType.STRING).description("생일 카페 진행 상태"),
                                fieldWithPath("specialGoodsStockState").type(JsonFieldType.STRING).description("생일 카페 굿즈 양"),
                                fieldWithPath("mainImage").type(JsonFieldType.STRING).description("생일 카페 메인 이미지 url"),
                                fieldWithPath("defaultImages.[]").type(JsonFieldType.ARRAY).description("생일 카페 기본 이미지 url")
                        )
                ));
    }

    @Test
    void 사장님이_생일_카페_신청_목록을_조회한다() throws Exception {
        given(birthdayCafeQueryService.findBirthdayCafeApplication(any(), any()))
                .willReturn(List.of(
                                new BirthdayCafeApplicationResponse(
                                        1L,
                                        "민혁",
                                        new BirthdayCafeApplicationResponse.ArtistResponse("샤이니", "민호"),
                                        LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                                        LocalDateTime.of(2024, 3, 23, 0, 0, 0)
                                ),
                                new BirthdayCafeApplicationResponse(
                                        2L,
                                        "더즈",
                                        new BirthdayCafeApplicationResponse.ArtistResponse(null, "아이유"),
                                        LocalDateTime.of(2024, 4, 20, 0, 0, 0),
                                        LocalDateTime.of(2024, 4, 23, 0, 0, 0)
                                )
                        )
                );

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/owners/birthday-cafes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("progressState", "RENTAL_PENDING")
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-application", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("progressState").description("생일 카페 진행 상태")
                        ),
                        responseFields(
                                fieldWithPath("[].birthdayCafeId").type(JsonFieldType.NUMBER).description("생일 카페 ID"),
                                fieldWithPath("[].hostName").type(JsonFieldType.STRING).description("주최자 닉네임"),
                                fieldWithPath("[].artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("[].artist.name").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("생일 카페 종료일")
                        )
                ));
    }

    @Test
    void 사장님이_생일_카페_신청_상세_조회를_한다() throws Exception {
        //given
        LoginMember loginMember = new LoginMember(1L);
        Long birthdayCafeId = 1L;
        given(birthdayCafeQueryService.findBirthdayCafeApplicationDetail(loginMember, birthdayCafeId))
                .willReturn(new BirthdayCafeApplicationDetailResponse(
                        birthdayCafeId,
                        new BirthdayCafeApplicationDetailResponse.ArtistResponse("방탄소년단", "뷔"),
                        LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                        LocalDateTime.of(2024, 3, 23, 0, 0, 0),
                        10,
                        20,
                        "@ChaseM",
                        "010-1234-5678"
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/owners/birthday-cafes/{birthdayCafeId}", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-application-detail", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("생일 카페 ID")
                        ),
                        responseFields(
                                fieldWithPath("birthdayCafeId").type(JsonFieldType.NUMBER).description("생일 카페 ID"),
                                fieldWithPath("artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("artist.name").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("생일 카페 종료일"),
                                fieldWithPath("minimumVisitant").type(JsonFieldType.NUMBER).description("생일 카페 최소 방문자 수"),
                                fieldWithPath("maximumVisitant").type(JsonFieldType.NUMBER).description("생일 카페 최대 방문자 수"),
                                fieldWithPath("twitterAccount").type(JsonFieldType.STRING).description("생일 카페 트위터 계정"),
                                fieldWithPath("hostPhoneNumber").type(JsonFieldType.STRING).description("주최자 전화번호")
                        )
                ));
    }

    @Test
    void 사장님이_생일_카페_상세_일정을_조회한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        String nickname = "주최자 닉네임";
        LoginMember loginMember = new LoginMember(1L);
        LocalDateTime date = LocalDateTime.of(2024, 3, 20, 0, 0, 0);
        given(birthdayCafeQueryService.findBirthdayCafeScheduleDetail(loginMember, date))
                .willReturn(
                        new BirthdayCafeScheduleDetailResponse(
                                birthdayCafeId,
                                nickname,
                                new BirthdayCafeScheduleDetailResponse.ArtistResponse("방탄소년단", "뷔"),
                                LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 23, 0, 0, 0)
                        ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/owners/birthday-cafes/schedules/detail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("date", String.valueOf(date))
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-schedules-detail", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("date").description("검색할 날짜")
                        ),
                        responseFields(
                                fieldWithPath("birthdayCafeId").type(JsonFieldType.NUMBER).description("생일 카페 ID"),
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("주최자 닉네임"),
                                fieldWithPath("artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("artist.name").type(JsonFieldType.STRING).description("아티스트 이름"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("생일 카페 종료일")
                        )
                ));
    }

    @Test
    void 사장님이_생일_카페_일정을_조회한다() throws Exception {
        // given
        LoginMember loginMember = new LoginMember(1L);
        int year = 2024;
        int month = 4;
        given(birthdayCafeQueryService.findBirthdayCafeSchedule(loginMember, year, month))
                .willReturn(
                        List.of(
                                new BirthdayCafeScheduleResponse(
                                        LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                                        LocalDateTime.of(2024, 3, 23, 0, 0, 0)
                                ),
                                new BirthdayCafeScheduleResponse(
                                        LocalDateTime.of(2024, 3, 24, 0, 0, 0),
                                        LocalDateTime.of(2024, 3, 25, 0, 0, 0)
                                )
                        )
                );

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/owners/birthday-cafes/schedules")
                        .contentType(MediaType.APPLICATION_JSON)
                        .queryParam("year", String.valueOf(year))
                        .queryParam("month", String.valueOf(month))
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-schedules", HOST_INFO, DOCUMENT_RESPONSE,
                        queryParameters(
                                parameterWithName("year").description("검색할 연도"),
                                parameterWithName("month").description("검색할 달")
                        ),
                        responseFields(
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("생일 카페 종료일")
                        )
                ));
    }
}
