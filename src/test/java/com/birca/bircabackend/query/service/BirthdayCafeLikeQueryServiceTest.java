package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.BirthdayCafeLikeResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/birthday-cafe-like-query-fixture.sql")
class BirthdayCafeLikeQueryServiceTest extends ServiceTest {

    @Autowired
    private BirthdayCafeLikeQueryService birthdayCafeLikeQueryService;

    @Test
    void 찜한_생일_카페_목록을_조회한다() {
        // given
        Long visitantId = 1L;

        // when
        List<BirthdayCafeLikeResponse> actual = birthdayCafeLikeQueryService.findLikedBirthdayCafes(visitantId);

        // then
        assertThat(actual).containsExactly(
                new BirthdayCafeLikeResponse(
                        1L,
                        "image1.com",
                        LocalDateTime.of(2024, 3, 18, 0, 0, 0),
                        LocalDateTime.of(2024, 3, 19, 0, 0, 0),
                        "민호 생일 카페",
                        "@ChaseM",
                        new BirthdayCafeLikeResponse.ArtistResponse("샤이니", "민호")
                ),
                new BirthdayCafeLikeResponse(
                        2L,
                        "image2.com",
                        LocalDateTime.of(2024, 3, 20, 0, 0, 0),
                        LocalDateTime.of(2024, 3, 23, 0, 0, 0),
                        "아이유 생일 카페",
                        "@ChaseM",
                        new BirthdayCafeLikeResponse.ArtistResponse(null, "아이유")
                )
        );
    }
}
