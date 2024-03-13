package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.SpecialGoodsResponse;
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
}
