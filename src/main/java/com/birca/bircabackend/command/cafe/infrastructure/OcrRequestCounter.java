package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.common.exception.BusinessException;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;

@Component
public class OcrRequestCounter {

    private final Map<Long, Integer> ocrRequestCounts = new ConcurrentHashMap<>();

    private static final int MAX_OCR_REQUEST_COUNT = 3;

    public void incrementOcrRequestCount(Long memberId) {
        int count = ocrRequestCounts.merge(memberId, 1, Integer::sum);
        if (count > MAX_OCR_REQUEST_COUNT) {
            throw BusinessException.from(OVER_MAX_OCR_REQUEST_COUNT);
        }
    }
}
