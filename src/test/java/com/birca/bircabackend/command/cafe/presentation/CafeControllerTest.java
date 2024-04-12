package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.patch;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CafeControllerTest extends DocumentationTest {

    @Test
    void 카페_상세_정보를_수정한다() throws Exception {
        // given
        CafeUpdateRequest request = new CafeUpdateRequest(
                "메가커피",
                "서울특별시 강남구 테헤란로 212",
                "@ChaseM",
                "8시 - 22시",
                List.of(new CafeUpdateRequest.CafeMenuResponse("아메리카노", 1500)),
                List.of(new CafeUpdateRequest.CafeOptionResponse("액자", 2000))
        );

        // when
        ResultActions result = mockMvc.perform(
                patch("/api/v1/cafes")
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
                                fieldWithPath("businessHours").type(JsonFieldType.STRING).description("카페 영업 시간"),
                                fieldWithPath("cafeMenus").type(JsonFieldType.ARRAY).description("카페 메뉴 리스트"),
                                fieldWithPath("cafeMenus[].name").type(JsonFieldType.STRING).description("메뉴명"),
                                fieldWithPath("cafeMenus[].price").type(JsonFieldType.NUMBER).description("가격"),
                                fieldWithPath("cafeOptions").type(JsonFieldType.ARRAY).description("카페 데코레이션 및 추가서비스 리스트"),
                                fieldWithPath("cafeOptions[].name").type(JsonFieldType.STRING).description("데코레이션 및 추가서비스 명"),
                                fieldWithPath("cafeOptions[].price").type(JsonFieldType.NUMBER).description("가격")
                        )
                ));
    }
}
