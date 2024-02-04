package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OcrRequestCounterTest extends ServiceTest {

    @Autowired
    private OcrRequestCounter ocrRequestCounter;

    @Test
    void OCR_요청_횟수를_초과하면_에러가_발생한다() {
        // given
        ocrRequestCounter.incrementOcrRequestCount(1L);
        ocrRequestCounter.incrementOcrRequestCount(1L);
        ocrRequestCounter.incrementOcrRequestCount(1L);

        // when then
        assertThatThrownBy(() -> ocrRequestCounter.incrementOcrRequestCount(1L))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(OVER_MAX_OCR_REQUEST_COUNT);
    }
}