package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;

class BirthdayCafeTest {

    private static final Long HOST_ID = 1L;
    private static final Long ARTIST_ID = 1L;
    private static final Long CAFE_ID = 1L;
    private static final Long CAFE_OWNER_ID = 2L;
    private static final Schedule SCHEDULE = Schedule.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
    private static final Visitants VISITANTS = Visitants.of(3, 10);
    private static final String TWITTER_ACCOUNT = "ChaseM";
    private static final PhoneNumber HOST_PHONE_NUMBER = PhoneNumber.from("010-0000-0000");

    @Nested
    @DisplayName("생일 카페는 카페 대관 신청 시")
    class ApplyRentalTest {


        @Test
        void 대관_대기_상태로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);

            // then
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_PENDING);
        }

        @Test
        void 공개_상태가_비공개로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);

            // then
            assertThat(actual.getVisibility()).isEqualTo(Visibility.PRIVATE);
        }

        @Test
        void 혼잡도가_원할로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);

            // then
            assertThat(actual.getCongestionState()).isEqualTo(CongestionState.SMOOTH);
        }

        @Test
        void 특전_재고는_많음으로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);

            // then
            assertThat(actual.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.ABUNDANT);
        }
    }

    @Nested
    @DisplayName("생일 카페 대관을 취소 시")
    class CancelTest {

        @Test
        void 주최자_취소한다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);

            // when
            birthdayCafe.cancelRental(HOST_ID);

            // then
            assertThat(birthdayCafe.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 사장님이_취소한다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);

            // when
            birthdayCafe.cancelRental(CAFE_OWNER_ID);

            // then
            assertThat(birthdayCafe.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 주최자도_사장님도_아니면_취소하지_못한다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.applyRental(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER);
            Long anotherMember = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafe.cancelRental(anotherMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_CANCEL);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "RENTAL_PENDING")
        void 대관_대기_상태가_아니면_취소하지_못한다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = new BirthdayCafe(
                    HOST_ID, ARTIST_ID, CAFE_ID, CAFE_OWNER_ID, SCHEDULE, VISITANTS, TWITTER_ACCOUNT, HOST_PHONE_NUMBER,
                    progressState, Visibility.PRIVATE, CongestionState.SMOOTH, SpecialGoodsStockState.ABUNDANT);

            // when then
            assertThatThrownBy(() -> birthdayCafe.cancelRental(HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_CANCEL_RENTAL);
        }
    }
}
