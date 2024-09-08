package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.CafeImageValidator;
import com.birca.bircabackend.command.cafe.exception.CafeImageErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/cafe-image-fixture.sql")
class CafeImageValidatorTest extends ServiceTest {

    @Autowired
    private CafeImageValidator cafeImageValidator;

    @Test
    void 카페_이미지가_5개가_초과되면_예외가_발생한다() {
        // given
        Long cafeId = 1L;
        List<MultipartFile> cafeImages = IntStream.rangeClosed(1, 11)
                .mapToObj(i -> {
                    String fileName = "image" + i + ".png";
                    byte[] content = fileName.getBytes(UTF_8);
                    return new MockMultipartFile("image" + i, content);
                })
                .collect(Collectors.toList());

        // when then
        assertThatThrownBy(() -> cafeImageValidator.validateImageSize(cafeImages, cafeId))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(CafeImageErrorCode.INVALID_UPLOAD_SIZE_REQUEST);
    }
}
