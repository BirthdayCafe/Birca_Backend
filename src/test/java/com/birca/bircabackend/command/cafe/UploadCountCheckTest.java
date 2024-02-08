package com.birca.bircabackend.command.cafe;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.support.enviroment.DocumentationTest;
import org.apache.http.HttpHeaders;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UploadCountCheckTest extends DocumentationTest {

    @Nested
    @DisplayName("사업자등록증 업로드 가능 횟수가")
    class UploadCountInterceptorTest {

        @Test
        void 초과되지_않으면_정상_처리된다() throws Exception {
            // given
            Long ownerId = 1L;
            OcrRequestHistory ocrRequestHistory = new OcrRequestHistory(ownerId, 0);
            given(ocrRequestHistoryRepository.findUploadCountByOwnerId(ownerId))
                    .willReturn(Optional.of(ocrRequestHistory.getUploadCount()));

            // when
            ResultActions result = mockMvc.perform(post("/test-upload-count")
                    .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(ownerId)));

            // then
            result.andExpect(status().isOk());
        }

        @Test
        void 초과되면_예외가_발생한다() throws Exception {
            // given
            Long ownerId = 1L;
            OcrRequestHistory ocrRequestHistory = new OcrRequestHistory(ownerId, 5);
            given(ocrRequestHistoryRepository.findUploadCountByOwnerId(ownerId))
                    .willReturn(Optional.of(ocrRequestHistory.getUploadCount()));

            // when
            ResultActions result = mockMvc.perform(post("/test-upload-count")
                    .header(HttpHeaders.AUTHORIZATION, bearerTokenProvider.getToken(ownerId)));

            // then
            result.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode", OVER_MAX_OCR_REQUEST_COUNT).exists());
        }
    }
}
