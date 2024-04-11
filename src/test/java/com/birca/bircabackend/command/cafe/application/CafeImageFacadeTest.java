package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/cafe-image-fixture.sql")
class CafeImageFacadeTest extends ServiceTest {

    @Autowired
    private CafeImageFacade cafeImageFacade;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("카페 이미지를 저장할 때")
    class UploadCafeImageTest {

        @Test
        void 정상적으로_저장한다() {
            // given
            Long cafeId = 2L;
            String fileName = "cafe-image.com";
            MultipartFile cafeImage = new MockMultipartFile(fileName, fileName.getBytes(StandardCharsets.UTF_8));

            // when
            cafeImageFacade.uploadCafeImage(cafeImage, cafeId);
            List<CafeImage> response = entityManager.createQuery(
                            "select ci from CafeImage ci where ci.cafeId = :cafeId", CafeImage.class
                    )
                    .setParameter("cafeId", cafeId)
                    .getResultList();

            // then
            assertThat(response.size()).isEqualTo(1);
        }

        @Test
        void 존재하지_않은_카페는_예외가_발생한다() {
            // given
            Long cafeId = 100L;
            String fileName = "cafe-image.com";
            MultipartFile cafeImage = new MockMultipartFile(fileName, fileName.getBytes(StandardCharsets.UTF_8));

            // when then
            assertThatThrownBy(() -> cafeImageFacade.uploadCafeImage(cafeImage, cafeId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }
}
