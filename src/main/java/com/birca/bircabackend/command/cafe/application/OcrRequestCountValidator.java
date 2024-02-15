package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OcrRequestCountValidator {

    private static final int MAX_UPLOAD_COUNT = 5;

    private final OcrRequestHistoryRepository ocrRequestHistoryRepository;

    public void validateUploadCount(Long ownerId) {
        ocrRequestHistoryRepository.findUploadCountByOwnerId(ownerId)
                .ifPresent(uploadCount -> {
                    if (uploadCount >= MAX_UPLOAD_COUNT) {
                        throw BusinessException.from(OVER_MAX_OCR_REQUEST_COUNT);
                    }
                });
    }
}
