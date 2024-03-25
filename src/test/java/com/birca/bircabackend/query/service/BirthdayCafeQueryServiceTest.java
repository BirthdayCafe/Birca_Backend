package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeQueryServiceTest extends ServiceTest {

    private static final Long BIRTHDAY_CAFE_ID = 2L;
    private static final Long HOST_ID = 1L;
    private static final Long VISITANT_ID = 4L;

    @Autowired
    private BirthdayCafeQueryService birthdayCafeQueryService;

    @Test
    void 생일_카페_특전_목록을_조회한다() {
        // when
        List<SpecialGoodsResponse> actual = birthdayCafeQueryService.findSpecialGoods(BIRTHDAY_CAFE_ID);

        // then
        assertThat(actual)
                .containsOnly(
                        new SpecialGoodsResponse("특전", "포토카드"),
                        new SpecialGoodsResponse("디저트", "포토카드, ID 카드")
                );
    }

    @Test
    void 생일_카페_메뉴_목록을_조회한다() {
        // when
        List<MenuResponse> actual = birthdayCafeQueryService.findMenus(BIRTHDAY_CAFE_ID);

        // then
        assertThat(actual)
                .containsOnly(
                        new MenuResponse("기본", "아메리카노+포토카드+ID카드", 6000),
                        new MenuResponse("디저트", "케이크+포토카드+ID카드", 10000)
                );
    }

    @Test
    void 생일_카페_럭키_드로우_목록을_조회한다() {
        // when
        List<LuckyDrawResponse> actual = birthdayCafeQueryService.findLuckyDraws(BIRTHDAY_CAFE_ID);

        // then
        assertThat(actual)
                .containsOnly(
                        new LuckyDrawResponse(1, "티셔츠"),
                        new LuckyDrawResponse(2, "머그컵")
                );
    }

    @Nested
    @DisplayName("주최자가 나의 생일 카페 목록을")
    class GetMyBirthdayCafesTest {

        @Test
        void 조회한다() {
            // given
            LoginMember loginMember = new LoginMember(HOST_ID);

            // when
            List<MyBirthdayCafeResponse> actual = birthdayCafeQueryService.findMyBirthdayCafes(loginMember);

            // then
            assertThat(actual).containsExactly(
                    new MyBirthdayCafeResponse(
                            2L,
                            "winter-cafe-main-image.com",
                            LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                            LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                            "윈터의 생일 카페",
                            "IN_PROGRESS",
                            new MyBirthdayCafeResponse.ArtistResponse("에스파", "윈터")
                    ),
                    new MyBirthdayCafeResponse(
                            1L,
                            null,
                            LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                            LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                            null,
                            "RENTAL_PENDING",
                            new MyBirthdayCafeResponse.ArtistResponse(null, "아이유")
                    )
            );
        }
    }

    @Nested
    @DisplayName("방문자가 생일 카페 목록을")
    class GetBirthdayCafesTest {

        private final BirthdayCafeParams birthdayCafeParams = new BirthdayCafeParams();
        private final PagingParams pagingParams = new PagingParams();

        @Test
        void 공개된_것만_시작일_순으로_조회한다() {
            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeResponse::birthdayCafeId)
                            .containsExactly(2L, 3L),
                    () -> assertThat(actual.get(0))
                            .isEqualTo(new BirthdayCafeResponse(
                                    2L,
                                    "winter-cafe-main-image.com",
                                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                                    "윈터의 생일 카페",
                                    false,
                                    new BirthdayCafeResponse.ArtistResponse("에스파", "윈터")
                            ))
            );
        }

        @Test
        void 찜한_생일_카페는_true로_표시한다() {
            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertThat(actual)
                    .filteredOn(BirthdayCafeResponse::isLiked)
                    .map(BirthdayCafeResponse::birthdayCafeId)
                    .containsExactly(3L);
        }
    }
}
