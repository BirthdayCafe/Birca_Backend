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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;

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
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.cancelRental(HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_CANCEL_RENTAL);
        }
    }

    @Nested
    @DisplayName("생일 카페 찜하기를")
    class BirthdayCafeLikeTest {

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = {"RENTAL_PENDING", "RENTAL_CANCELED"})
        void 누른다(ProgressState progressState) {
            // given
            Long visitantId = 1L;
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .build();

            // when
            BirthdayCafeLike birthdayCafeLike = birthdayCafe.like(visitantId);

            // then
            assertThat(birthdayCafeLike.getBirthdayCafeId()).isEqualTo(birthdayCafe.getId());
            assertThat(birthdayCafeLike.getVisitantId()).isEqualTo(1L);
        }

        @ParameterizedTest
        @EnumSource(mode = INCLUDE, names = {"RENTAL_PENDING", "RENTAL_CANCELED"})
        void 대관_대기_상태나_취소_상태의_생일_카페는_누를_수_없다(ProgressState progressState) {
            // given
            Long visitantId = 1L;

            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.like(visitantId))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_LIKE_REQUEST);
        }
    }

    @Nested
    @DisplayName("특전 재고 상태 변경은")
    class SpecialGoodsStateChangeTest {

        @Test
        void 진행_중인_생일_카페면_가능하다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.IN_PROGRESS)
                    .specialGoodsStockState(SpecialGoodsStockState.ABUNDANT)
                    .build();

            // when
            birthdayCafe.changeState(SpecialGoodsStockState.SCARCE, HOST_ID);

            // then
            assertThat(birthdayCafe.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.SCARCE);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "IN_PROGRESS")
        void 진행_중이_아닌_생일_카페면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .specialGoodsStockState(SpecialGoodsStockState.ABUNDANT)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(SpecialGoodsStockState.SCARCE, HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.IN_PROGRESS)
                    .specialGoodsStockState(SpecialGoodsStockState.ABUNDANT)
                    .build();

            assertThatThrownBy(() -> birthdayCafe.changeState(SpecialGoodsStockState.SCARCE, 100L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("혼잡도 상태 변경은")
    class CongestionStateChangeTest {
        @Test
        void 진행_중인_생일_카페면_가능하다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.IN_PROGRESS)
                    .congestionState(CongestionState.SMOOTH)
                    .build();

            // when
            birthdayCafe.changeState(CongestionState.MODERATE, HOST_ID);

            // then
            assertThat(birthdayCafe.getCongestionState()).isEqualTo(CongestionState.MODERATE);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "IN_PROGRESS")
        void 진행_중이_아닌_생일_카페면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .congestionState(CongestionState.SMOOTH)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(CongestionState.MODERATE, HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.IN_PROGRESS)
                    .congestionState(CongestionState.SMOOTH)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(CongestionState.MODERATE, 100L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("공개 상태 변경은")
    class VisibilityChangeTest {

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "RENTAL_PENDING")
        void 대관_대기가_아닌_생일_카페면_가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .visibility(Visibility.PRIVATE)
                    .build();

            // when
            birthdayCafe.changeState(Visibility.PUBLIC, HOST_ID);

            // then
            assertThat(birthdayCafe.getVisibility()).isEqualTo(Visibility.PUBLIC);
        }

        @Test
        void 대관_대기인_생일_카페면_불가능하다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.RENTAL_PENDING)
                    .visibility(Visibility.PRIVATE)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(Visibility.PUBLIC, HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.IN_PROGRESS)
                    .visibility(Visibility.PRIVATE)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(Visibility.PUBLIC, 100L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 특전 등록은")
    class SpecialGoodsTest {

        private final List<SpecialGoods> specialGoods = List.of(
                new SpecialGoods("기본", "ID, 포토카드"),
                    new SpecialGoods("기본", "ID, 포토카드")
            );

        @ParameterizedTest
        @EnumSource(mode = INCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중일_때만_가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .build();

            // when
            birthdayCafe.registerSpecialGoods(HOST_ID, specialGoods);

            // then
            assertThat(birthdayCafe.getSpecialGoods()).isEqualTo(specialGoods);
        }

        @Test
        void 기존의_특전을_완전히_대체한다() {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.RENTAL_APPROVED)
                    .specialGoods(List.of(new SpecialGoods("기존 특전", "포토카드")))
                    .build();

            // when
            birthdayCafe.registerSpecialGoods(HOST_ID, specialGoods);

            // then
            assertThat(birthdayCafe.getSpecialGoods()).isEqualTo(specialGoods);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중이_아니면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(progressState)
                    .build();

            // when then
            assertThatThrownBy(() -> birthdayCafe.registerSpecialGoods(HOST_ID, specialGoods))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            BirthdayCafe birthdayCafe = BirthdayCafe.builder()
                    .hostId(HOST_ID)
                    .progressState(ProgressState.IN_PROGRESS)
                    .build();

            // then
            assertThatThrownBy(() -> birthdayCafe.registerSpecialGoods(100L, specialGoods))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }
}
