package com.birca.bircabackend.command.birca.application;


import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/birthday-cafe-image-fixture.sql")
class BirthdayCafeImageValidatorTest extends ServiceTest {

    @Autowired
    private BirthdayCafeImageValidator birthdayCafeImageValidator;

    @Test
    void 이미지_수가_10이_초과하면_예외가_발생한다() {
        // given
        Long birthdayCafeId = 1L;
        List<MultipartFile> birthdayCafeImages = IntStream.rangeClosed(1, 7)
                .mapToObj(i -> {
                    String fileName = "image" + i + ".png";
                    byte[] content = fileName.getBytes(UTF_8);
                    return new MockMultipartFile("image" + i, content);
                })
                .collect(toList());

        //when then
        assertThatThrownBy(() -> birthdayCafeImageValidator.validateImagesSize(birthdayCafeId, birthdayCafeImages))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(BirthdayCafeErrorCode.INVALID_UPLOAD_SIZE_REQUEST);
    }
}
