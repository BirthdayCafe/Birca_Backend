package com.birca.bircabackend.command.cafe.infrastructure.uploadcount;

import com.birca.bircabackend.command.cafe.application.OcrRequestCountProvider;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Transactional
@RequiredArgsConstructor
public class OcrRequestCountProviderImpl implements OcrRequestCountProvider {

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;

    @Override
    public UploadCountResponse getUploadCount(Long ownerId) {
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
