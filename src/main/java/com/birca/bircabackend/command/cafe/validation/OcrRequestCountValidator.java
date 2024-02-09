package com.birca.bircabackend.command.cafe.validation;

import com.birca.bircabackend.command.cafe.application.OcrRequestCountProvider;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistory;
import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;

@Component
@Transactional
@RequiredArgsConstructor
public class OcrRequestCountValidator implements OcrRequestCountProvider {

    private static final int MAX_UPLOAD_LIMIT = 5;

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

    public void validateUploadCount(Integer uploadCount) {
        if (uploadCount >= MAX_UPLOAD_LIMIT) {
            throw BusinessException.from(OVER_MAX_OCR_REQUEST_COUNT);
        }
    }
}
