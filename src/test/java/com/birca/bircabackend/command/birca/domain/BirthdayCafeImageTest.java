package com.birca.bircabackend.command.birca.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdayCafeImageTest {

    private static final Long BIRTHDAY_CAFE_ID = 1L;
    private static final String IMAGE_URL = "cafe.png";

    @Nested
    @DisplayName("생일 카페 이미지 생성할 때")
    class createBirthdayCafeImageTest {

        @Test
        void 기본_이미지는_false로_생성된다() {
            // when
            BirthdayCafeImage birthdayCafeImage = BirthdayCafeImage.createDefaultImage(BIRTHDAY_CAFE_ID, IMAGE_URL);

            // then
            assertThat(birthdayCafeImage.getIsMain()).isFalse();
            assertThat(birthdayCafeImage.getBirthdayCafeId()).isEqualTo(1L);
            assertThat(birthdayCafeImage.getImageUrl()).isEqualTo(IMAGE_URL);
        }

        @Test
        void 대표_이미지는_true로_생성된다() {
            // when
            BirthdayCafeImage birthdayCafeImage = BirthdayCafeImage.createMainImage(BIRTHDAY_CAFE_ID, IMAGE_URL);

            // then
            assertThat(birthdayCafeImage.getIsMain()).isTrue();
            assertThat(birthdayCafeImage.getBirthdayCafeId()).isEqualTo(1L);
            assertThat(birthdayCafeImage.getImageUrl()).isEqualTo(IMAGE_URL);
        }
    }

    @Test
    void 생일_카페_대표_이미지를_변경한다() {
        // given
        BirthdayCafeImage birthdayCafeImage = BirthdayCafeImage.createMainImage(BIRTHDAY_CAFE_ID, IMAGE_URL);

        // when
        birthdayCafeImage.updateMainImage("mega-coffee.png");

        // then
        assertThat(birthdayCafeImage.getImageUrl()).isEqualTo("mega-coffee.png");
    }
}
