package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.BirthdayCafeLikeResponse;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static com.birca.bircabackend.query.dto.BirthdayCafeLikeResponse.ArtistResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BirthdayCafeLikeQueryControllerTest extends DocumentationTest {

    @Test
    void 찜한_생일_카페_목록을_조회한다() throws Exception {
        // given
        given(birthdayCafeLikeQueryService.findLikedBirthdayCafes(any()))
                .willReturn(List.of(
                        new BirthdayCafeLikeResponse(
                                1L,
                                "image1.com",
                                LocalDateTime.of(2024, 3, 18, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 19, 0, 0, 0),
                                "민호의 생일 카페",
                                "ChaseM",
                                new ArtistResponse("샤이니", "민호")
                        ),
                        new BirthdayCafeLikeResponse(
                                2L,
                                "image2.com",
                                LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                                LocalDateTime.of(2024, 3, 23, 0, 0, 0),
                                "아이유의 생일 카페",
                                "ChaseM",
                                new ArtistResponse(null, "아이유")
                        )
                ));

        // when
        ResultActions result = mockMvc.perform(
                get("/api/v1/birthday-cafes/like")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("get-birthday-cafe-likes", HOST_INFO, DOCUMENT_RESPONSE,
                        responseFields(
                                fieldWithPath("[].birthdayCafeId").type(JsonFieldType.NUMBER).description("찜한 생일 카페 ID"),
                                fieldWithPath("[].mainImageUrl").type(JsonFieldType.STRING).description("찜한 생일 카페 메인 이미지 Url"),
                                fieldWithPath("[].startDate").type(JsonFieldType.STRING).description("찜한 생일 카페 시작일"),
                                fieldWithPath("[].endDate").type(JsonFieldType.STRING).description("찜한 생일 카페 종료일"),
                                fieldWithPath("[].birthdayCafeName").type(JsonFieldType.STRING).description("찜한 생일 카페 이름"),
                                fieldWithPath("[].twitterAccount").type(JsonFieldType.STRING).description("찜한 생일 카페 트위터 계정"),
                                fieldWithPath("[].artist.groupName").type(JsonFieldType.STRING).description("아티스트 그룹 이름").optional(),
                                fieldWithPath("[].artist.name").type(JsonFieldType.STRING).description("아티스트 이름")
                        )
                ));
    }
}
