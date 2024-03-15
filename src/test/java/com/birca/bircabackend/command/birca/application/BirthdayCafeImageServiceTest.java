package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
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
class BirthdayCafeImageServiceTest extends ServiceTest {

    @Autowired
    private BirthdayCafeImageService birthdayCafeImageService;

    @PersistenceContext
    private EntityManager em;

    @Nested
    @DisplayName("생일 카페 이미지를 업로드 시")
    class UploadBirthdayCafeImageTest {

        @Test
        void 정상적으로_저장한다() {
            // given
            Long birthdayCafeId = 1L;
            List<MultipartFile> birthdayCafeImages = List.of(
                    new MockMultipartFile("mega-coffee.png", "mega-coffee.png".getBytes(UTF_8)),
                    new MockMultipartFile("compose-coffee.png", "compose-coffee.png".getBytes(UTF_8))
            );

            // when
            birthdayCafeImageService.save(birthdayCafeId, birthdayCafeImages);
            List<BirthdayCafeImage> actual = em.createQuery(
                            "select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId", BirthdayCafeImage.class)
                    .setParameter("birthdayCafeId", 1L)
                    .getResultList();

            // then
            Assertions.assertThat(actual.size()).isEqualTo(2);
            String birthdayCafeImage = actual.get(0).getBirthdayCafeImage();
            System.out.println("birthdayCafeImage = " + birthdayCafeImage);
        }

        @Test
        void 이미지_수가_10이_초과하면_예외가_발생한다() {
            // given
            Long birthdayCafeId = 1L;
            List<MultipartFile> birthdayCafeImages = IntStream.rangeClosed(1, 11)
                    .mapToObj(i -> {
                        String fileName = "image" + i + ".png";
                        byte[] content = fileName.getBytes(UTF_8);
                        return new MockMultipartFile("image" + i, content);
                    })
                    .collect(toList());

            //when then
            assertThatThrownBy(() -> birthdayCafeImageService.save(birthdayCafeId, birthdayCafeImages))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPLOAD_SIZE_REQUEST);
        }
    }
}
