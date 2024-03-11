package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.dto.MenuRequest;
import com.birca.bircabackend.command.birca.dto.SpecialGoodsRequest;
import com.birca.bircabackend.command.birca.dto.StateChangeRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BirthdayCafeControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 생일_카페를_등록한다() throws Exception {
        // given
        ApplyRentalRequest request = new ApplyRentalRequest(
                1L,
                1L,
                LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                5,
                10,
                "@ChaseM",
                "010-0000-0000"
        );

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/birthday-cafes")
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("register-birthday-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        requestFields(
                                fieldWithPath("artistId").type(JsonFieldType.NUMBER).description("생일인 아티스트 ID"),
                                fieldWithPath("cafeId").type(JsonFieldType.NUMBER).description("생일 카페를 진행할 카페 ID"),
                                fieldWithPath("startDate").type(JsonFieldType.STRING).description("생일 카페 시작일"),
                                fieldWithPath("endDate").type(JsonFieldType.STRING).description("생일 카페 마지막일"),
                                fieldWithPath("minimumVisitant").type(JsonFieldType.NUMBER).description("생일 카페 최소 방문자 인원"),
                                fieldWithPath("maximumVisitant").type(JsonFieldType.NUMBER).description("생일 카페 최대 방문자 인원"),
                                fieldWithPath("twitterAccount").type(JsonFieldType.STRING).description("생일 카페 트위터 계정"),
                                fieldWithPath("hostPhoneNumber").type(JsonFieldType.STRING).description("주최자 연락처")
                        )
                ));
    }

    @Test
    void 생일_카페_대관을_취소한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/birthday-cafes/{birthdayCafeId}/cancel", birthdayCafeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("cancel-birthday-cafe", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("취소할 생일 카페 ID")
                        )
                ));
    }

    @Test
    void 생일_카페_상태를_변경한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        String stateName = "congestion";
        StateChangeRequest request = new StateChangeRequest("MODERATE");

        // when
        ResultActions result = mockMvc.perform(
                patch("/api/v1/birthday-cafes/{birthdayCafeId}/{stateName}", birthdayCafeId, stateName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("birthday-cafe-state-update", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("취소할 생일 카페 ID"),
                                parameterWithName("stateName")
                                        .description("변경할 상태 이름 (specialGoods, congestion, visibility)")
                        ),
                        requestFields(
                                fieldWithPath("state").type(JsonFieldType.STRING).description("변경할 상태 값")
                        )
                ));
    }

    @Test
    void 생일_카페_특전을_추가한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        List<SpecialGoodsRequest> request = List.of(
                new SpecialGoodsRequest("기본", "종이컵, 포토카드"),
                new SpecialGoodsRequest("디저트", "종이컵, 포토카드, ID카드")
        );

        // when
        ResultActions result = mockMvc.perform(
                post("/api/v1/birthday-cafes/{birthdayCafeId}/special-goods", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("birthday-cafe-special-goods", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("특전을 추가할 생일 카페 ID")
                        ),
                        requestFields(
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("특전의 이름"),
                                fieldWithPath("[].details").type(JsonFieldType.STRING).description("특전의 세부 내용")
                        )
                ));
    }

    @Test
    void 생일_카페_메뉴를_추가한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        List<MenuRequest> request = List.of(
                new MenuRequest("기본", "아메리카노+포토카드+ID카드", 10000),
                new MenuRequest("디저트", "케이크+포토카드+ID카드", 10000)
        );

        // when
        ResultActions result = mockMvc.perform(
                post("/api/v1/birthday-cafes/{birthdayCafeId}/menus", birthdayCafeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("birthday-cafe-menus", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("메뉴를 추가할 생일 카페 ID")
                        ),
                        requestFields(
                                fieldWithPath("[].name").type(JsonFieldType.STRING).description("메뉴의 이름"),
                                fieldWithPath("[].details").type(JsonFieldType.STRING).description("메뉴의 세부 내용"),
                                fieldWithPath("[].price").type(JsonFieldType.NUMBER).description("메뉴의 가격")
                        )
                ));
    }

    @Test
    void 생일_카페_에러_코드() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/error-codes")
                .queryParam("className", "com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("birthday-cafe-error-Code",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        responseFields(getErrorDescriptor(BirthdayCafeErrorCode.values()))
                ));
    }
}
