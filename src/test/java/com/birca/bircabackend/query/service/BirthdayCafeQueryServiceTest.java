package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.value.CongestionState;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.domain.value.SpecialGoodsStockState;
import com.birca.bircabackend.command.birca.domain.value.Visibility;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeQueryServiceTest extends ServiceTest {

    private static final Long BIRTHDAY_CAFE_ID = 4L;
    private static final Long HOST_ID = 1L;
    private static final Long VISITANT_ID = 4L;

    @Autowired
    private BirthdayCafeQueryService birthdayCafeQueryService;

    @Nested
    @DisplayName("생일 카페 목록 중")
    class GetBirthdayCafeItemsTest {

        @Test
        void 특전_목록을_조회한다() {
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
        void 메뉴_목록을_조회한다() {
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
        void 럭키_드로우_목록을_조회한다() {
            // when
            List<LuckyDrawResponse> actual = birthdayCafeQueryService.findLuckyDraws(BIRTHDAY_CAFE_ID);

            // then
            assertThat(actual)
                    .containsOnly(
                            new LuckyDrawResponse(1, "티셔츠"),
                            new LuckyDrawResponse(2, "머그컵")
                    );
        }
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
            assertThat(actual).map(MyBirthdayCafeResponse::birthdayCafeId)
                    .containsExactly(4L, 3L, 1L);
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
                            .containsExactly(2L, 3L, 4L, 5L, 6L)
            );
        }

        @Test
        void 조회_시_찜한_생일_카페는_true로_표시한다() {
            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertThat(actual)
                    .filteredOn(BirthdayCafeResponse::isLiked)
                    .map(BirthdayCafeResponse::birthdayCafeId)
                    .containsExactly(3L, 6L);
        }

        @Test
        void 실시간_생일_카페만_조회한다() {
            // given
            String progressState = "IN_PROGRESS";
            birthdayCafeParams.setProgressState(progressState);

            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeResponse::birthdayCafeId)
                            .containsExactly(4L, 6L)
            );
        }

        @Test
        void 특정_아티스트의_카페만_조회한다() {
            // given
            long artistId = 3L;
            birthdayCafeParams.setArtistId(artistId);

            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeResponse::birthdayCafeId)
                            .containsExactly(4L, 5L)
            );
        }

        @Test
        void 특정_카페에서_진행_되는_생일_카페만_조회한다() {
            // given
            long cafeId = 1L;
            birthdayCafeParams.setCafeId(cafeId);

            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeResponse::birthdayCafeId)
                            .containsExactly(2L, 3L, 5L)
            );
        }

        @Test
        void 지정한_개수만큼_조회한다() {
            // given
            int size = 3;
            pagingParams.setSize(size);

            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeResponse::birthdayCafeId)
                            .containsExactly(2L, 3L, 4L)
            );
        }

        @Test
        void 커서_이후로_조회한다() {
            // given
            long cursor = 3L;
            pagingParams.setCursor(cursor);

            // when
            List<BirthdayCafeResponse> actual = birthdayCafeQueryService.findBirthdayCafes(
                    birthdayCafeParams, pagingParams, new LoginMember(VISITANT_ID));

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeResponse::birthdayCafeId)
                            .containsExactly(4L, 5L, 6L)
            );
        }
    }


    @Nested
    @DisplayName("생일 카페 상세를 조회할 때")
    class findBirthdayCafeDetailTest {

        private final LoginMember loginMember = new LoginMember(4L);

        @Test
        void 정상적으로_조회한다() {
            // given
            Long birthdayCafeId = 3L;

            // when
            BirthdayCafeDetailResponse actual = birthdayCafeQueryService.findBirthdayCafeDetail(loginMember, birthdayCafeId);

            // then
            assertThat(actual).isEqualTo(
                    new BirthdayCafeDetailResponse(
                            new BirthdayCafeDetailResponse.CafeResponse(
                                    "메가커피", "서울특별시 강남구 테헤란로 212", List.of("image1.com", "image2.com")
                            ),
                            new BirthdayCafeDetailResponse.ArtistResponse(null, "아이유"),
                            LocalDateTime.parse("2024-02-09T00:00"),
                            LocalDateTime.parse("2024-02-10T00:00"),
                            "아이유의 생일 카페",
                            1,
                            true,
                            "@ChaseM",
                            5,
                            10,
                            CongestionState.SMOOTH,
                            Visibility.PUBLIC,
                            ProgressState.FINISHED,
                            SpecialGoodsStockState.ABUNDANT,
                            "iu-cafe-main-image.com",
                            List.of("iu-cafe-default-image.com", "iu-cafe-default-image.com")
                    )
            );
        }

        @Test
        void 존재하지_않는_생일_카페는_예외가_발생한다() {
            // given
            Long birthdayCafeId = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeQueryService.findBirthdayCafeDetail(loginMember, birthdayCafeId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("사장님이 생일 카페 신청 목록을")
    class FindBirthdayCafeApplication {

        @Test
        void 조회한다() {
            // given
            LoginMember loginMember = new LoginMember(3L);
            String progressState = "RENTAL_PENDING";

            // when
            List<BirthdayCafeApplicationResponse> actual = birthdayCafeQueryService.findBirthdayCafeApplication(loginMember, progressState);

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeApplicationResponse::birthdayCafeId)
                            .containsExactly(1L)
            );
        }
    }

    @Nested
    @DisplayName("사장님이 생일 카페 신청 상세할 때")
    class FindBirthdayCafeApplicationDetail {

        @Test
        void 정상적으로_조회한다() {
            // given
            LoginMember loginMember = new LoginMember(3L);
            Long birthdayCafeId = 1L;

            // when
            BirthdayCafeApplicationDetailResponse response =
                    birthdayCafeQueryService.findBirthdayCafeApplicationDetail(loginMember, birthdayCafeId);

            // then
            assertAll(
                    () -> assertThat(response.birthdayCafeId()).isEqualTo(1L),
                    () -> assertThat(response.artist().groupName()).isNull(),
                    () -> assertThat(response.artist().name()).isEqualTo("아이유"),
                    () -> assertThat(response.startDate()).isEqualTo("2024-02-08T00:00:00"),
                    () -> assertThat(response.endDate()).isEqualTo("2024-02-10T00:00:00"),
                    () -> assertThat(response.minimumVisitant()).isEqualTo(5),
                    () -> assertThat(response.maximumVisitant()).isEqualTo(10),
                    () -> assertThat(response.twitterAccount()).isEqualTo("@ChaseM"),
                    () -> assertThat(response.hostPhoneNumber()).isEqualTo("010-0000-0000")
            );
        }

        @Test
        void 존재하지_않는_생일_카페는_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);
            Long birthdayCafeId = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeQueryService.findBirthdayCafeApplicationDetail(loginMember, birthdayCafeId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("사장님이 생일 카페 일정을")
    class FindBirthdayCafeScheduleTest {

        @Test
        void 조회한다() {
            // given
            int year = 2024;
            int month = 2;

            // when
            List<BirthdayCafeScheduleResponse> actual = birthdayCafeQueryService.findBirthdayCafeSchedule(year, month);

            // then
            assertAll(
                    () -> assertThat(actual)
                            .map(BirthdayCafeScheduleResponse::birthdayCafeId)
                            .containsExactly(1L, 2L, 3L, 4L, 5L, 6L)
            );
        }
    }
}
