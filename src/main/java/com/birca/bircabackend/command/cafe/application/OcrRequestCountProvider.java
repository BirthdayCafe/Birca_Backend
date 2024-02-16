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
        OcrRequestHistory ocrRequestHistory = getOcrRequestHistory(ownerId);
        Integer uploadCount = ocrRequestHistory.increaseUploadCount();
        return new UploadCountResponse(uploadCount);
    }

    private OcrRequestHistory getOcrRequestHistory(Long ownerId) {
        return ocrRequestHistoryRepository.findByOwnerId(ownerId)
                .orElseGet(() -> {
                    OcrRequestHistory newOcrRequestHistory = new OcrRequestHistory(ownerId);
                    ocrRequestHistoryRepository.save(newOcrRequestHistory);
                    return newOcrRequestHistory;
                });
    }
}
