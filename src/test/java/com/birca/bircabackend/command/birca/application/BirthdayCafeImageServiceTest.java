package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/birthday-cafe-image-fixture.sql")
class BirthdayCafeImageServiceTest extends ServiceTest {

    @Autowired
    private BirthdayCafeImageService birthdayCafeImageService;

    @PersistenceContext
    private EntityManager em;

    @Test
    void 생일_카페_기본_이미지를_저장한다() {
        // given
        Long birthdayCafeId = 2L;
        String imageUrl = "mega-coffee.png";

        // when
        birthdayCafeImageService.saveDefaultImage(birthdayCafeId, imageUrl);
        List<BirthdayCafeImage> actual = em.createQuery(
                        "select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId", BirthdayCafeImage.class)
                .setParameter("birthdayCafeId", birthdayCafeId)
                .getResultList();

        // then
        assertThat(actual.size()).isEqualTo(4);
        assertThat(actual)
                .extracting(BirthdayCafeImage::getIsMain)
                .containsExactlyElementsOf(List.of(false, false, false, false));
    }

    @Nested
    @DisplayName("생일 카페 대표 이미지를 저장할 때")
    class saveMainImageTest {

        @Test
        void 존재하면_변경한다() {
            // given
            Long birthdayCafeId = 1L;
            String imageUrl = "update-cafe.png";

            // when
            birthdayCafeImageService.updateMainImage(birthdayCafeId, imageUrl);
            BirthdayCafeImage actual = em.createQuery(
                            "select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId and bci.isMain = :isMain", BirthdayCafeImage.class)
                    .setParameter("birthdayCafeId", birthdayCafeId)
                    .setParameter("isMain", true)
                    .getSingleResult();

            // then
            assertThat(actual.getImageUrl()).isEqualTo(imageUrl);
        }

        @Test
        void 존재하지_않으면_새로_저장한다() {
            // given
            Long birthdayCafeId = 2L;
            String imageUrl = "new-cafe.png";

            // when
            birthdayCafeImageService.updateMainImage(birthdayCafeId, imageUrl);
            BirthdayCafeImage actual = em.createQuery(
                            "select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId and bci.isMain = :isMain", BirthdayCafeImage.class)
                    .setParameter("birthdayCafeId", birthdayCafeId)
                    .setParameter("isMain", true)
                    .getSingleResult();

            // then
            assertThat(actual.getImageUrl()).isEqualTo(imageUrl);
        }
    }

    @Test
    void 생일_카페_이미지를_삭제한다() {
        // given
        Long birthdayCafeId = 1L;
        String imageUrl = "image1.com";

        // when
        birthdayCafeImageService.delete(birthdayCafeId, imageUrl);
        Long count = em.createQuery(
                        "select count(*) from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId", Long.class)
                .setParameter("birthdayCafeId", birthdayCafeId)
                .getSingleResult();

        // then
        assertThat(count).isEqualTo(10);
    }
}
