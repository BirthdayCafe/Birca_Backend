package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OcrRequestCountValidator {

    @Value("${ocr.request-count}")
    private int maxUploadCount;

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;

    public void validateUploadCount(Long ownerId) {
        ocrRequestHistoryRepository.findUploadCountByOwnerId(ownerId)
                .ifPresent(uploadCount -> {
                    if (uploadCount >= maxUploadCount) {
                        throw BusinessException.from(OVER_MAX_OCR_REQUEST_COUNT);
                    }
                });
    }
}
