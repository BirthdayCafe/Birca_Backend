package com.birca.bircabackend.command.cafe.infrastructure.uploadcount;

import com.birca.bircabackend.command.cafe.domain.OcrRequestHistoryRepository;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Optional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.DUPLICATE_BUSINESS_LICENSE_NUMBER;
import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.OVER_MAX_OCR_REQUEST_COUNT;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

class OcrRequestCountValidatorTest extends ServiceTest {

    @Autowired
    private OcrRequestCountValidator ocrRequestCountValidator;

    @Test
    @Sql("/fixture/ocr-request-history-fixture.sql")
    void 사업자등록증_업로드_요청_횟수가_5이상이면_예외가_발생한다() {
        // given
        Long ownerId = 1L;

        // when, then
        assertThatThrownBy(() -> ocrRequestCountValidator.validateUploadCount(ownerId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(OVER_MAX_OCR_REQUEST_COUNT);
    }

    @Test
    void 사업자등록증_업로드_요청_횟수가_5미만이면_정상_처리된다() {
        // given
        Long ownerId = 1L;

        // when, then
        assertThatCode(() -> ocrRequestCountValidator.validateUploadCount(ownerId))
                .doesNotThrowAnyException();
    }
}
