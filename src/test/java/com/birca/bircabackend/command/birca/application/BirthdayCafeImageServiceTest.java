package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
    void 생일_카페_이미지를_저장한다() {
        // given
        Long birthdayCafeId = 2L;
        String imageUrl = "mega-coffee.png";

        // when
        birthdayCafeImageService.save(birthdayCafeId, imageUrl);
        List<BirthdayCafeImage> actual = em.createQuery(
                        "select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId", BirthdayCafeImage.class)
                .setParameter("birthdayCafeId", 2L)
                .getResultList();

        // then
        assertThat(actual.size()).isEqualTo(4);
        assertThat(actual)
                .extracting(BirthdayCafeImage::getIsMain)
                .containsExactlyElementsOf(List.of(false, false, false, false));
    }
}
