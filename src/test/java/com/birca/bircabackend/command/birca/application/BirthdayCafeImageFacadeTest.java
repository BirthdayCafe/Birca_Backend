package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeImageFacadeTest extends ServiceTest {

    @Autowired
    protected BirthdayCafeImageFacade birthdayCafeImageFacade;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    void 생일_카페_이미지를_S3에_업로드하고_저장한다() {
        // given
        Long birthdayCafeId = 1L;
        List<MultipartFile> birthdayCafeImages = List.of(
                new MockMultipartFile("birthdayCafeImage", "image1.png", MediaType.IMAGE_PNG_VALUE, "image1".getBytes()),
                new MockMultipartFile("birthdayCafeImage", "image2.png", MediaType.IMAGE_PNG_VALUE, "image2".getBytes())
        );

        // when
        birthdayCafeImageFacade.save(birthdayCafeId, birthdayCafeImages);
        List<BirthdayCafeImage> images = entityManager.createQuery("select bci from BirthdayCafeImage bci", BirthdayCafeImage.class)
                .getResultList();

        // then
        assertThat(images.size()).isEqualTo(2);
    }
}
