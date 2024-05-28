package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.command.cafe.exception.CafeImageErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/cafe-image-fixture.sql")
class CafeImageServiceTest extends ServiceTest {

    @Autowired
    private CafeImageService cafeImageService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("카페 이미지를 저장할 때")
    class UploadCafeImageTest {

        @Test
        void 정상적으로_저장한다() {
            // given
            Long cafeId = 2L;
            String imageUrl = "cafe-image.com";

            // when
            cafeImageService.save(cafeId, imageUrl);
            List<CafeImage> response = entityManager.createQuery(
                            "select ci from CafeImage ci where ci.cafeId = :cafeId", CafeImage.class
                    )
                    .setParameter("cafeId", cafeId)
                    .getResultList();

            // then
            assertThat(response.size()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("카페 이미지를 삭제할 때")
    class DeleteCafeImageTest {

        @Test
        void 정상적으로_삭제한다() {
            // given
            Long cafeId = 1L;

            // when
            cafeImageService.delete(cafeId);
            List<CafeImage> response = entityManager.createQuery(
                            "select ci from CafeImage ci where ci.cafeId = :cafeId", CafeImage.class
                    )
                    .setParameter("cafeId", cafeId)
                    .getResultList();

            // then
            assertThat(response.size()).isEqualTo(0);
        }
    }
}
