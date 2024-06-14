package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
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

    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .build();

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

        private final BirthdayCafe rentalPendingCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                .set("hostId", HOST_ID)
                .set("cafeOwnerId", CAFE_OWNER_ID)
                .set("progressState", ProgressState.RENTAL_PENDING)
                .sample();

        @Test
        void 주최자_취소한다() {
            // when
            rentalPendingCafe.cancelRental(HOST_ID);

            // then
            assertThat(rentalPendingCafe.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 사장님이_취소한다() {
            // when
            rentalPendingCafe.cancelRental(CAFE_OWNER_ID);

            // then
            assertThat(rentalPendingCafe.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 주최자도_사장님도_아니면_취소하지_못한다() {
            // given
            Long anotherMember = 100L;

            // when then
            assertThatThrownBy(() -> rentalPendingCafe.cancelRental(anotherMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_CANCEL);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "RENTAL_PENDING")
        void 대관_대기_상태가_아니면_취소하지_못한다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("cafeOwnerId", CAFE_OWNER_ID)
                    .set("progressState", progressState)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.cancelRental(HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_CANCEL_RENTAL);
        }
    }

    @Nested
    @DisplayName("특전 재고 상태 변경은")
    class SpecialGoodsStateChangeTest {

        @Test
        void 진행_중인_생일_카페면_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .set("specialGoodsStockState", SpecialGoodsStockState.ABUNDANT)
                    .sample();

            // when
            birthdayCafe.changeState(SpecialGoodsStockState.SCARCE, HOST_ID);

            // then
            assertThat(birthdayCafe.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.SCARCE);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "IN_PROGRESS")
        void 진행_중이_아닌_생일_카페면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .set("specialGoodsStockState", SpecialGoodsStockState.ABUNDANT)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(SpecialGoodsStockState.SCARCE, HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .set("specialGoodsStockState", SpecialGoodsStockState.ABUNDANT)
                    .sample();

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
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .set("congestionState", CongestionState.SMOOTH)
                    .sample();

            // when
            birthdayCafe.changeState(CongestionState.MODERATE, HOST_ID);

            // then
            assertThat(birthdayCafe.getCongestionState()).isEqualTo(CongestionState.MODERATE);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = "IN_PROGRESS")
        void 진행_중이_아닌_생일_카페면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .set("congestionState", CongestionState.SMOOTH)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(CongestionState.MODERATE, HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .set("congestionState", CongestionState.SMOOTH)
                    .sample();

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
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .set("visibility", Visibility.PRIVATE)
                    .sample();

            // when
            birthdayCafe.changeState(Visibility.PUBLIC, HOST_ID);

            // then
            assertThat(birthdayCafe.getVisibility()).isEqualTo(Visibility.PUBLIC);
        }

        @Test
        void 대관_대기인_생일_카페면_불가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.RENTAL_PENDING)
                    .set("visibility", Visibility.PRIVATE)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.changeState(Visibility.PUBLIC, HOST_ID))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .set("visibility", Visibility.PRIVATE)
                    .sample();

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
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .sample();

            // when
            birthdayCafe.replaceSpecialGoods(HOST_ID, specialGoods);

            // then
            assertThat(birthdayCafe.getSpecialGoods()).isEqualTo(specialGoods);
        }

        @Test
        void 기존의_특전을_완전히_대체한다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.RENTAL_APPROVED)
                    .set("specialGoods", List.of(new SpecialGoods("기존 특전", "포토카드")))
                    .sample();

            // when
            birthdayCafe.replaceSpecialGoods(HOST_ID, specialGoods);

            // then
            assertThat(birthdayCafe.getSpecialGoods()).isEqualTo(specialGoods);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중이_아니면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.replaceSpecialGoods(HOST_ID, specialGoods))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.replaceSpecialGoods(100L, specialGoods))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 메뉴 등록은")
    class BirthdayCafeMenuTest {

        private final List<BirthdayCafeMenu> birthdayCafeMenus = List.of(
                BirthdayCafeMenu.of("기본", "아메리카노+포토카드+ID카드", 10000),
                BirthdayCafeMenu.of("디저트", "케이크+포토카드+ID카드", 10000)
        );

        @ParameterizedTest
        @EnumSource(mode = INCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중일_때만_가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .sample();

            // when
            birthdayCafe.replaceMenus(HOST_ID, birthdayCafeMenus);

            // then
            assertThat(birthdayCafe.getBirthdayCafeMenus()).isEqualTo(birthdayCafeMenus);
        }

        @Test
        void 기존의_메뉴를_완전히_대체한다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.RENTAL_APPROVED)
                    .set("menus", List.of(BirthdayCafeMenu.of("기존 메뉴", "포토카드", 7000)))
                    .sample();

            // when
            birthdayCafe.replaceMenus(HOST_ID, birthdayCafeMenus);

            // then
            assertThat(birthdayCafe.getBirthdayCafeMenus()).isEqualTo(birthdayCafeMenus);
        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중이_아니면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.replaceMenus(HOST_ID, birthdayCafeMenus))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.replaceMenus(100L, birthdayCafeMenus))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 럭키 드로우 등록은")
    class LuckyDrawTest {

        private final List<LuckyDraw> luckyDraws = List.of(
                new LuckyDraw(1, "머그컵"),
                new LuckyDraw(2, "포토 카드")
        );

        @ParameterizedTest
        @EnumSource(mode = INCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중일_때만_가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .sample();

            // when
            birthdayCafe.replaceLuckyDraws(HOST_ID, luckyDraws);

            // then
            assertThat(birthdayCafe.getLuckyDraws()).isEqualTo(luckyDraws);
        }

        @Test
        void 기존의_럭키_드로우를_완전히_대체한다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .set("luckyDraws", List.of(new LuckyDraw(1, "머그컵")))
                    .sample();

            // when
            birthdayCafe.replaceLuckyDraws(HOST_ID, luckyDraws);

            // then
            assertThat(birthdayCafe.getLuckyDraws()).isEqualTo(luckyDraws);

        }

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = {"RENTAL_APPROVED", "IN_PROGRESS"})
        void 대관_승인됨과_진행_중이_아니면_불가능하다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", progressState)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.replaceLuckyDraws(HOST_ID, luckyDraws))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .set("progressState", ProgressState.IN_PROGRESS)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.replaceLuckyDraws(100L, luckyDraws))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 이름 수정은")
    class UpdateNameTest {

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .sample();
            String name = "생일 카페 이름";

            // when
            birthdayCafe.updateName(HOST_ID, name);

            // then
            assertThat(birthdayCafe.getName()).isEqualTo(name);
        }

        @Test
        void 주최자가_아니면_할_수_없다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .sample();
            String name = "생일 카페 이름";

            // when then
            assertThatThrownBy(() -> birthdayCafe.updateName(100L, name))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 이름 수정은")
    class UpdateTwitterAccountTest {

        @Test
        void 주최자만_가능하다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .sample();
            String twitterAccount = "@twitterAccount";

            // when
            birthdayCafe.updateTwitterAccount(HOST_ID, twitterAccount);

            // then
            assertThat(birthdayCafe.getTwitterAccount()).isEqualTo(twitterAccount);
        }

        @Test
        void 주최자가_아니면_할_수_없다() {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("hostId", HOST_ID)
                    .sample();
            String twitterAccount = "@twitterAccount";

            // when then
            assertThatThrownBy(() -> birthdayCafe.updateTwitterAccount(100L, twitterAccount))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 진행 상태를 변경할 때")
    class ChangeProgressStateTest {

        @Test
        void 요청_승인으로_변경한다() {
            // given
            Long ownerId = 1L;
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("cafeOwnerId", ownerId)
                    .sample();

            // when
            birthdayCafe.approveRental(ownerId);

            // then
            assertThat(birthdayCafe.getProgressState()).isEqualTo(ProgressState.RENTAL_APPROVED);
        }

        @ParameterizedTest
        @CsvSource({
                "NOW, MAX, IN_PROGRESS, PUBLIC",
                "MIN, NOW, FINISHED, PRIVATE"
        })
        void 진행이나_종료로_변경한다(String start, String end, String progressState, String visibility) {
            // given
            LocalDateTime startDate = start.equals("NOW") ? LocalDateTime.now() :
                    start.equals("MIN") ? LocalDateTime.MIN :
                            LocalDateTime.parse(start);
            LocalDateTime endDate = end.equals("NOW") ? LocalDateTime.now() :
                    end.equals("MAX") ? LocalDateTime.MAX :
                            LocalDateTime.parse(end);
            Schedule schedule = Schedule.of(startDate, endDate);
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("schedule", schedule)
                    .set("visibility", Visibility.PUBLIC)
                    .sample();

            // when
            birthdayCafe.changeState(schedule);
            ProgressState actualState = birthdayCafe.getProgressState();
            Visibility actualVisibility = birthdayCafe.getVisibility();
            ProgressState expectedProgressState = ProgressState.valueOf(progressState);
            Visibility expectedVisibility = Visibility.valueOf(visibility);

            // then
            assertAll(
                    () -> assertThat(actualState).isEqualTo(expectedProgressState),
                    () -> assertThat(actualVisibility).isEqualTo(expectedVisibility)
            );
        }

        @Test
        void 자신의_카페가_아니면_수락할_수_없다() {
            // given
            Long ownerId = 1L;
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("cafeOwnerId", ownerId)
                    .sample();

            // when then
            assertThatThrownBy(() -> birthdayCafe.approveRental(100L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }
}
