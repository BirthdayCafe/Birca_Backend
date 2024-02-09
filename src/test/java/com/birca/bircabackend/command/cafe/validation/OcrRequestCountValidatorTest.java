package com.birca.bircabackend.command.cafe.validation;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OcrRequestCountValidatorTest {

    @Mock
    private OcrRequestHistoryRepository ocrRequestHistoryRepository;

    @InjectMocks
    private OcrRequestCountValidator ocrRequestCountValidator;

    @Test
    void 사업자등록증_업로드_횟수를_증가시킨다() {
        // given
        Long ownerId = 1L;
        OcrRequestHistory ocrRequestHistory = new OcrRequestHistory(ownerId, 0);
        ocrRequestHistory.incrementUploadCount();

        when(ocrRequestHistoryRepository.findByOwnerId(ownerId))
                .thenReturn(Optional.of(ocrRequestHistory));

        // when
        UploadCountResponse uploadCountResponse = ocrRequestCountValidator.getUploadCount(ownerId);

        // then
        assertThat(uploadCountResponse.uploadCount()).isEqualTo(2);
    }

    @Test
    void 기존_업로드_횟수가_없을때_사업자등록증_업로드_횟수가_증가한다() {
        // given
        Long ownerId = 1L;
        when(ocrRequestHistoryRepository.findByOwnerId(ownerId))
                .thenReturn(Optional.empty());

        // when
        UploadCountResponse uploadCountResponse = ocrRequestCountValidator.getUploadCount(ownerId);

        // then
        assertThat(uploadCountResponse.uploadCount()).isEqualTo(1);
    }


    @Test
    void 사업자등록증_업로드_요청_횟수가_5이상이면_예외가_발생한다() {
        // given
        Integer uploadCount = 5;

        // when then
        assertThatThrownBy(() -> ocrRequestCountValidator.validateUploadCount(uploadCount))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(OVER_MAX_OCR_REQUEST_COUNT);
    }

    @Test
    void 사업자등록증_업로드_요청_횟수가_5미만이면_정상_처리된다() {
        // given
        Integer uploadCount = 3;

        // when then
        assertThatCode(() -> ocrRequestCountValidator.validateUploadCount(uploadCount))
                .doesNotThrowAnyException();
    }
}
