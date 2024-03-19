package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BirthdayCafeImageControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 생일_카페_기본_이미지를_저장한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        MockMultipartFile defaultImage = new MockMultipartFile("defaultImage",  "defaultImage".getBytes());

        // when
        ResultActions result = mockMvc.perform(multipart("/api/v1/birthday-cafes/{birthdayCafeId}/images", birthdayCafeId)
                .file(defaultImage)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)
                ));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("upload-birthday-cafe-default-image",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        requestParts(
                                partWithName("defaultImage").description("생일 카페 기본 이미지")
                        )
                ));
    }

    @Test
    void 생일_카페_대표_이미지를_저장한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        MockMultipartFile mainImage = new MockMultipartFile("mainImage",  "mainImage".getBytes());

        // when
        ResultActions result = mockMvc.perform(multipart("/api/v1/birthday-cafes/{birthdayCafeId}/images/main", birthdayCafeId)
                .file(mainImage)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)
                ));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("upload-birthday-cafe-main-image",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        requestParts(
                                partWithName("mainImage").description("생일 카페 대표 이미지")
                        )
                ));
    }
}
