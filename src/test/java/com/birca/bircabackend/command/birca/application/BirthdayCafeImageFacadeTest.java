package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeImageFacadeTest extends ServiceTest {

    @Autowired
    protected BirthdayCafeImageFacade birthdayCafeImageFacade;

    @PersistenceContext
    private EntityManager entityManager;

    private static final MultipartFile BIRTHDAY_CAFE_IMAGE =
            new MockMultipartFile("birthdayCafeImage",  "image1".getBytes());

    @Nested
    @DisplayName("생일 카페 이미지 업로드 시")
    class UploadTest {

        @Test
        void 정상적으로_저장한다() {
            // given
            Long birthdayCafeId = 1L;

            // when
            birthdayCafeImageFacade.save(birthdayCafeId, BIRTHDAY_CAFE_IMAGE);
            List<BirthdayCafeImage> images = entityManager.createQuery("select bci from BirthdayCafeImage bci", BirthdayCafeImage.class)
                    .getResultList();

            // then
            assertThat(images.size()).isEqualTo(1);
            verify(imageUploader, times(1)).upload(any());
        }

        @Test
        void 존재하지_생일_카페는_예외가_발생한다() {
            // given
            Long birthdayCafeId = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeImageFacade.save(birthdayCafeId, BIRTHDAY_CAFE_IMAGE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }
    }
}
