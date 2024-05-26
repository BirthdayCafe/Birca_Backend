package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.command.cafe.dto.CafeImageDeleteRequest;
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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

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
            List<MultipartFile> cafeImages = IntStream.rangeClosed(1, 4)
                    .mapToObj(i -> {
                        String fileName = "image" + i + ".png";
                        byte[] content = fileName.getBytes(UTF_8);
                        return new MockMultipartFile("image" + i, content);
                    })
                    .collect(Collectors.toList());

            // when
            cafeImageFacade.uploadCafeImage(cafeImages, cafeId);
            List<CafeImage> response = entityManager.createQuery(
                            "select ci from CafeImage ci where ci.cafeId = :cafeId", CafeImage.class
                    )
                    .setParameter("cafeId", cafeId)
                    .getResultList();

            // then
            assertThat(response.size()).isEqualTo(4);
            verify(imageRepository, times(4)).upload(any());
        }

        @Test
        void 존재하지_않은_카페는_예외가_발생한다() {
            // given
            Long cafeId = 100L;
            List<MultipartFile> cafeImages = IntStream.rangeClosed(1, 11)
                    .mapToObj(i -> {
                        String fileName = "image" + i + ".png";
                        byte[] content = fileName.getBytes(UTF_8);
                        return new MockMultipartFile("image" + i, content);
                    })
                    .collect(Collectors.toList());

            // when then
            assertThatThrownBy(() -> cafeImageFacade.uploadCafeImage(cafeImages, cafeId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("카페 이미지를 삭제할 때")
    class DeleteCafeImageTest {

        @Test
        void 정상적으로_삭제한다() {
            // given
            Long cafeId = 1L;
            String imageUrl = "image1.com";
            CafeImageDeleteRequest request = new CafeImageDeleteRequest(imageUrl);

            // when
            cafeImageFacade.deleteCafeImage(cafeId, request);
            List<CafeImage> response = entityManager.createQuery("select ci from CafeImage ci where ci.cafeId = :cafeId", CafeImage.class)
                    .setParameter("cafeId", cafeId)
                    .getResultList();

            // then
            assertThat(response.size()).isEqualTo(4);
            verify(imageRepository, times(1)).delete(any());
        }

        @Test
        void 존재하지_않은_카페는_예외가_발생한다() {
            // given
            Long cafeId = 100L;
            String imageUrl = "cafe-image.com";
            CafeImageDeleteRequest request = new CafeImageDeleteRequest(imageUrl);

            // when then
            assertThatThrownBy(() -> cafeImageFacade.deleteCafeImage(cafeId, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }
}
