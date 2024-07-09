package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.dto.MemoRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MemoControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 생일_카페_내용을_메모한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        MemoRequest request = new MemoRequest("생일 카페 메모 내용");

        // when
        ResultActions result = mockMvc.perform(post("/api/v1/{birthdayCafeId}/memo", birthdayCafeId)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID))
                .content(objectMapper.writeValueAsString(request))
        );

        // then
        result.andExpect((status().isOk()))
                .andDo(document("save-birthday-cafe-memo", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("birthdayCafeId").description("메모할 생일 카페 ID")
                        ),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("생일 카페 메모 내용")
                        )
                ));
    }
}
