package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.command.cafe.dto.DayOffCreateRequest;
import com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CafeControllerTest extends DocumentationTest {

    @Test
    void 카페_기본_정보를_수정한다() throws Exception {
        // given
        CafeUpdateRequest request = new CafeUpdateRequest(
                "메가커피",
                "서울특별시 강남구 테헤란로 212",
                "@ChaseM",
                "8시 - 22시"
        );

        // when
        ResultActions result = mockMvc.perform(
                post("/api/v1/cafes")
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("cafe-update", HOST_INFO, DOCUMENT_RESPONSE,
                        requestFields(
                                fieldWithPath("cafeName").type(JsonFieldType.STRING).description("카페 이름"),
                                fieldWithPath("cafeAddress").type(JsonFieldType.STRING).description("카페 주소"),
                                fieldWithPath("twitterAccount").type(JsonFieldType.STRING).description("카페 트위터 계정"),
                                fieldWithPath("businessHours").type(JsonFieldType.STRING).description("카페 영업 시간")
                        )
                ));
    }

    @Test
    void 카페_휴무일을_설정한다() throws Exception {
        // given
        DayOffCreateRequest request = new DayOffCreateRequest(
                List.of(
                        LocalDateTime.of(2024, 5, 5, 0, 0, 0),
                        LocalDateTime.of(2024, 5, 15, 0, 0, 0)
                )
        );

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/cafes/{cafeId}/day-off", 1L)
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("cafe-day-off", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("cafeId").description("카페 ID")
                        ),
                        requestFields(
                                fieldWithPath("datOffDates.[]").type(JsonFieldType.ARRAY).description("카페 휴무일 목록")
                        )
                ));
    }

    @Test
    void 카페_에러_코드() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/error-codes")
                .queryParam("className", "com.birca.bircabackend.command.cafe.exception.CafeErrorCode")
                .contentType(MediaType.APPLICATION_JSON)
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("cafe-error-Code",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        responseFields(getErrorDescriptor(CafeErrorCode.values()))
                ));
    }
}
