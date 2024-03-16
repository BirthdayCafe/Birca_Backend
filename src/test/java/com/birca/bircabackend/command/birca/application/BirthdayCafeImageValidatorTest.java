package com.birca.bircabackend.command.birca.application;


import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/birthday-cafe-image-fixture.sql")
class BirthdayCafeImageValidatorTest extends ServiceTest {

    @Autowired
    private BirthdayCafeImageValidator birthdayCafeImageValidator;

    @Test
    void 이미지_수가_10이_초과하면_예외가_발생한다() {
        // given
        Long birthdayCafeId = 1L;

        //when then
        assertThatThrownBy(() -> birthdayCafeImageValidator.validateImagesSize(birthdayCafeId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(BirthdayCafeErrorCode.INVALID_UPLOAD_SIZE_REQUEST);
    }
}
