package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Sql("/fixture/birthday-cafe-image-fixture.sql")
class BirthdayCafeImageFacadeTest extends ServiceTest {

    @Autowired
    private BirthdayCafeImageFacade birthdayCafeImageFacade;

    private static final MultipartFile BIRTHDAY_CAFE_IMAGE =
            new MockMultipartFile("birthdayCafeImage",  "image1".getBytes());

    @Nested
    @DisplayName("생일 카페 기본 이미지 업로드 시")
    class UploadDefaultImageTest {

        @Test
        void 정상적으로_저장한다() {
            // given
            Long birthdayCafeId = 2L;

            // when
            birthdayCafeImageFacade.updateDefaultImage(birthdayCafeId, BIRTHDAY_CAFE_IMAGE);

            // then
            verify(imageUploader, times(1)).upload(any());
        }

        @Test
        void 기본_이미지수가_10이_초과되면_예외가_발생한다() {
            // given
            Long birthdayCafeId = 1L;

            // when then
            assertThatThrownBy(() -> birthdayCafeImageFacade.updateDefaultImage(birthdayCafeId, BIRTHDAY_CAFE_IMAGE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPLOAD_SIZE_REQUEST);
        }

        @Test
        void 존재하지_생일_카페는_예외가_발생한다() {
            // given
            Long birthdayCafeId = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeImageFacade.updateDefaultImage(birthdayCafeId, BIRTHDAY_CAFE_IMAGE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("생일 카페 대표 이미지 업로드 시")
    class uploadMainImageTest {

        @Test
        void 존재하면_변경한다() {
            // given
            Long birthdayCafeId = 1L;

            // when
            birthdayCafeImageFacade.updateMainImage(birthdayCafeId, BIRTHDAY_CAFE_IMAGE);

            // then
            verify(imageUploader, times(1)).upload(any());
            verify(imageUploader, times(1)).delete(any());
        }

        @Test
        void 존재하지_않으면_새로_저장한다() {
            // given
            Long birthdayCafeId = 2L;

            // when
            birthdayCafeImageFacade.updateMainImage(birthdayCafeId, BIRTHDAY_CAFE_IMAGE);
            verify(imageUploader, times(1)).upload(any());
            verify(imageUploader, times(0)).delete(any());
        }

        @Test
        void 존재하지_생일_카페는_예외가_발생한다() {
            // given
            Long birthdayCafeId = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeImageFacade.updateMainImage(birthdayCafeId, BIRTHDAY_CAFE_IMAGE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }
    }
}
