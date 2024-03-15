package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class BirthdayCafeImageControllerTest extends DocumentationTest {

    private static final Long MEMBER_ID = 1L;

    @Test
    void 생일_카페_이미지를_저장한다() throws Exception {
        // given
        Long birthdayCafeId = 1L;
        List<MockMultipartFile> birthdayCafeImages = List.of(
                new MockMultipartFile("birthdayCafeImage", "image1.png", MediaType.IMAGE_PNG_VALUE, "image1".getBytes()),
                new MockMultipartFile("birthdayCafeImage", "image2.png", MediaType.IMAGE_PNG_VALUE, "image2".getBytes())
        );

        // when
        ResultActions result = mockMvc.perform(multipart("/api/v1/birthday-cafes/{birthdayCafeId}/images", birthdayCafeId)
                .file(birthdayCafeImages.get(0))
                .file(birthdayCafeImages.get(1))
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(MEMBER_ID)
                ));

        // then
        result.andExpect((status().isOk()))
                .andDo(document("upload-birthday-cafe-image",
                        HOST_INFO,
                        DOCUMENT_RESPONSE,
                        requestParts(
                                partWithName("birthdayCafeImage").description("생일 카페 이미지")
                        )
                ));
    }
}
