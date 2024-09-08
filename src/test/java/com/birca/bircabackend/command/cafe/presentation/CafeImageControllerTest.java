package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.cafe.dto.CafeImageDeleteRequest;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CafeImageControllerTest extends DocumentationTest {

    private static final Long CAFE_ID = 1L;
    private static final Long MEMBER_ID = 1L;

    @Test
    void 카페_이미지를_업로드한다() throws Exception {
        // given
        MockMultipartFile cafeImage = new MockMultipartFile("cafe-image",  "cafe-image".getBytes());

        // when
        ResultActions result = mockMvc.perform(
                multipart("/api/v1/cafes/{cafeId}/images", CAFE_ID)
                        .file(cafeImage)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("upload-cafe-image", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("cafeId").description("카페 ID")
                        )
                ));
    }
}
