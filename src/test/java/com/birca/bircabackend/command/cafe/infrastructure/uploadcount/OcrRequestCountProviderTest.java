package com.birca.bircabackend.command.cafe.infrastructure.uploadcount;

import com.birca.bircabackend.command.cafe.application.OcrRequestCountProvider;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class OcrRequestCountProviderTest extends ServiceTest {

    @Autowired
    private OcrRequestCountProvider ocrRequestCountProvider;

    @MockBean
    private OcrRequestHistoryRepository ocrRequestHistoryRepository;

    @Test
    void 사업자등록증_업로드_횟수를_증가시킨다() {
        // given
        Long ownerId = 1L;
        OcrRequestHistory ocrRequestHistory = new OcrRequestHistory(ownerId, 0);
        ocrRequestHistory.incrementUploadCount();

        when(ocrRequestHistoryRepository.findByOwnerId(ownerId))
                .thenReturn(Optional.of(ocrRequestHistory));

        // when
        UploadCountResponse uploadCountResponse = ocrRequestCountProvider.getUploadCount(ownerId);

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
        UploadCountResponse uploadCountResponse = ocrRequestCountProvider.getUploadCount(ownerId);

        // then
        assertThat(uploadCountResponse.uploadCount()).isEqualTo(1);
    }
}