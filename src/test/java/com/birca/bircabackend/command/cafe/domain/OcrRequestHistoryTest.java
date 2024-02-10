package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.INVALID_UPLOAD_COUNT;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class OcrRequestHistoryTest {

    @Test
    void 사업자등록증_업로드_횟수가_0이_아니면_예외가_발생한다() {
        // when then
        assertThatThrownBy(() -> new OcrRequestHistory(1L, 5))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(INVALID_UPLOAD_COUNT);
    }
}
