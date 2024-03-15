package com.birca.bircabackend.command.birca.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdayCafeImageTest {

    @Test
    void 생일_카페_저장_시_대표_이미지는_false로_생성된다() {
        // given
        Long birthdayCafeId = 1L;
        String imageUrl = "cafe.png";

        // when
        BirthdayCafeImage birthdayCafeImage = new BirthdayCafeImage(birthdayCafeId, imageUrl);

        // then
        assertThat(birthdayCafeImage.getIsMain()).isFalse();
    }
}
