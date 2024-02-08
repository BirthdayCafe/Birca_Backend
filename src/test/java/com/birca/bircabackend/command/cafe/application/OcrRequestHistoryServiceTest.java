package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

class OcrRequestHistoryServiceTest extends ServiceTest {

    @Autowired
    private OcrRequestHistoryService ocrRequestHistoryService;

    @Autowired
    private OcrRequestHistoryRepository ocrRequestHistoryRepository;

    @Test
    void 사업자등록증_업로드_횟수를_증가시킨다() {
        // given
        Long ownerId = 1L;
        OcrRequestHistory ocrRequestHistory = new OcrRequestHistory(ownerId, 1);
        ocrRequestHistoryRepository.save(ocrRequestHistory);

        // when
        UploadCountResponse uploadCountResponse = ocrRequestHistoryService.incrementUploadCount(ownerId);

        // then
        assertThat(uploadCountResponse.uploadCount()).isEqualTo(2);
    }

    @Test
    void 기존_업로드_횟수가_없을때_사업자등록증_업로드_횟수가_증가한다() {
        // given
        Long ownerId = 1L;

        // when
        UploadCountResponse uploadCountResponse = ocrRequestHistoryService.incrementUploadCount(ownerId);

        // then
        assertThat(uploadCountResponse.uploadCount()).isEqualTo(1);
    }
}