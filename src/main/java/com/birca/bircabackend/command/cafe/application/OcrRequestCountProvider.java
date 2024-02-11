package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class OcrRequestCountProvider {

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;

    public UploadCountResponse increaseUploadCount(Long ownerId) {
        OcrRequestHistory ocrRequestHistory = ocrRequestHistoryRepository.findByOwnerId(ownerId)
                .orElseGet(() -> {
                    OcrRequestHistory newOcrRequestHistory = new OcrRequestHistory(ownerId);
                    ocrRequestHistoryRepository.save(newOcrRequestHistory);
                    return newOcrRequestHistory;
                });
        Integer uploadCount = ocrRequestHistory.incrementUploadCount();
        return new UploadCountResponse(uploadCount);
    }
}
