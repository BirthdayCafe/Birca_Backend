package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
