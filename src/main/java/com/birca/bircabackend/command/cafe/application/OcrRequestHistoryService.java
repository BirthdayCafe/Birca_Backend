package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OcrRequestHistoryService {

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;

    public UploadCountResponse incrementUploadCount(Long ownerId) {
        OcrRequestHistory ocrRequestHistory = ocrRequestHistoryRepository.findByOwnerId(ownerId)
                .orElseGet(() -> {
                    OcrRequestHistory newOcrRequestHistory = new OcrRequestHistory(ownerId, 0);
                    ocrRequestHistoryRepository.save(newOcrRequestHistory);
                    return newOcrRequestHistory;
                });
        Integer uploadCount = ocrRequestHistory.incrementUploadCount();
        return new UploadCountResponse(uploadCount);
    }
}
