package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CafeImageControllerTest extends DocumentationTest {

    @Test
    void 카페_이미지를_업로드한다() throws Exception {
        // given
        Long cafeId = 1L;
        MockMultipartFile cafeImage = new MockMultipartFile("cafe-image",  "cafe-image".getBytes());

        // when
        ResultActions result = mockMvc.perform(
                multipart("/api/v1/cafes/{cafeId}/images", cafeId)
                        .file(cafeImage)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(1L)));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("upload-cafe-image", HOST_INFO, DOCUMENT_RESPONSE,
                        pathParameters(
                                parameterWithName("cafeId").description("카페 ID")
                        )
                ));
    }
}
