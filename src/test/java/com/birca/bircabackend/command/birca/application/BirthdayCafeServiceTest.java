package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.dto.*;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.command.cafe.exception.DayOffErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeServiceTest extends ServiceTest {

    private static final LoginMember HOST1 = new LoginMember(1L);
    private static final LoginMember HOST2 = new LoginMember(2L);
    private static final LoginMember ANOTHER_MEMBER = new LoginMember(2L);
    private static final LoginMember CAFE_1_OWNER = new LoginMember(3L);
    private static final LoginMember CAFE_2_OWNER = new LoginMember(5L);
    private static final long ARTIST_ID = 1L;
    private static final long CAFE1_ID = 1L;
    private static final long CAFE2_ID = 2L;

    private static final Long PENDING_BIRTHDAY_CAFE_ID = 1L;
    private static final Long IN_PROGRESS_BIRTHDAY_CAFE_ID = 4L;
    private static final Long RENTAL_APPROVED_BIRTHDAY_CAFE_ID = 5L;

    private static final ApplyRentalRequest VALID_REQUEST = new ApplyRentalRequest(
            ARTIST_ID,
            CAFE1_ID,
            LocalDateTime.of(2024, 2, 8, 0, 0, 0),
            LocalDateTime.of(2024, 2, 10, 0, 0, 0),
            5,
            10,
            "@ChaseM",
            "010-0000-0000"
    );

    @Autowired
    private BirthdayCafeService birthdayCafeService;

    @PersistenceContext
    private EntityManager entityManager;


    @Nested
    @DisplayName("생일 카페를 위해 카페를 대관할 때")
    class ApplyRentalTest {

        @Test
        void 정상적으로_신청한다() {
            // when
            birthdayCafeService.applyRental(VALID_REQUEST, HOST2);

            // then
            BirthdayCafe birthdayCafe = entityManager.find(BirthdayCafe.class, 1L);
            assertAll(
                    () -> assertThat(birthdayCafe.getArtistId()).isEqualTo(VALID_REQUEST.artistId()),
                    () -> assertThat(birthdayCafe.getHostId()).isEqualTo(HOST1.id()),
                    () -> assertThat(birthdayCafe.getVisitants())
                            .isEqualTo(Visitants.of(VALID_REQUEST.minimumVisitant(), VALID_REQUEST.maximumVisitant())),
                    () -> assertThat(birthdayCafe.getTwitterAccount()).isEqualTo(VALID_REQUEST.twitterAccount()),
                    () -> assertThat(birthdayCafe.getProgressState()).isEqualTo(ProgressState.RENTAL_PENDING),
                    () -> assertThat(birthdayCafe.getVisibility()).isEqualTo(Visibility.PRIVATE),
                    () -> assertThat(birthdayCafe.getCongestionState()).isEqualTo(CongestionState.SMOOTH),
                    () -> assertThat(birthdayCafe.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.ABUNDANT)
            );
        }

        @Test
        void 시작일이_종료일보다_앞일_수_없다() {
            // given
            LocalDateTime startDate = LocalDateTime.of(2024, 2, 11, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 2, 10, 0, 0, 0);
            ApplyRentalRequest request = new ApplyRentalRequest(
                    ARTIST_ID,
                    CAFE1_ID,
                    startDate,
                    endDate,
                    5,
                    10,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST2))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_SCHEDULE);
        }

        @Test
        void 최소_방문자는_최대_방문자_보다_클_수_없다() {
            // given
            int minimumVisitant = 11;
            int maximumVisitant = 10;
            ApplyRentalRequest request = new ApplyRentalRequest(
                    ARTIST_ID,
                    CAFE1_ID,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    minimumVisitant,
                    maximumVisitant,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST2))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }

        @Test
        void 최소_방문자와_최대_방문자는_양수여야_한다() {
            // given
            int minimumVisitant = -1;
            int maximumVisitant = -1;
            ApplyRentalRequest request = new ApplyRentalRequest(
                    ARTIST_ID,
                    CAFE1_ID,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    minimumVisitant,
                    maximumVisitant,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST2))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }

        @Test
        void 이미_대관_대기_생일_카페가_있으면_또_대관할_수_없다() {
            // given
            ApplyRentalRequest request = new ApplyRentalRequest(
                    ARTIST_ID,
                    CAFE2_ID,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    5,
                    10,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.RENTAL_PENDING_EXISTS);
        }
    }

    @Nested
    @DisplayName("생일 카페 대관 신청을 취소할 때")
    class CancelRentalTest {

        @Test
        void 주최자가_취소한다() {
            // when
            birthdayCafeService.cancelRental(PENDING_BIRTHDAY_CAFE_ID, HOST1);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, PENDING_BIRTHDAY_CAFE_ID);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 카페_사장님이_취소한다() {
            // when
            birthdayCafeService.cancelRental(PENDING_BIRTHDAY_CAFE_ID, CAFE_1_OWNER);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, PENDING_BIRTHDAY_CAFE_ID);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 카페_사장님이_대관_완료된_카페를_취소한다() {
            // when
            birthdayCafeService.cancelRental(RENTAL_APPROVED_BIRTHDAY_CAFE_ID, CAFE_1_OWNER);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, RENTAL_APPROVED_BIRTHDAY_CAFE_ID);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @ParameterizedTest
        @CsvSource({"2, 3", "4, 3"})
        void 대관_대기_상태이거나_완료_상태가_아니면_예외가_발생한다(Long birthdayCafeId, Long cafeOwnerId) {
            // given
            LoginMember owner = new LoginMember(cafeOwnerId);

            // when then
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(birthdayCafeId, owner))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_CANCEL_RENTAL);
        }

        @Test
        void 없는_생일_카페면_예외가_발생한다() {
            // given
            long notExistCafe = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(notExistCafe, HOST1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }

        @Test
        void 생일_카페_주최자_또는_카페_사장님이_아니면_취소할_수_없다() {
            // when // than
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(PENDING_BIRTHDAY_CAFE_ID, ANOTHER_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_CANCEL);
        }
    }

    @Nested
    @DisplayName("생일 카페 상태에서")
    class ChangeStateTest {
        private final Long inProgressCafeId = 4L;

        @Test
        void 특전_재고_상태를_변경한다() {
            // given
            StateChangeRequest request = new StateChangeRequest("SCARCE");

            // when
            birthdayCafeService.changeSpecialGoodsStockState(inProgressCafeId, HOST1, request);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, inProgressCafeId);
            assertThat(actual.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.SCARCE);
        }

        @Test
        void 혼잡도를_변경한다() {
            // given
            StateChangeRequest request = new StateChangeRequest("CONGESTED");

            // when
            birthdayCafeService.changeCongestionState(inProgressCafeId, HOST1, request);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, inProgressCafeId);
            assertThat(actual.getCongestionState()).isEqualTo(CongestionState.CONGESTED);
        }

        @Test
        void 공개_상태를_변경한다() {
            // given
            StateChangeRequest request = new StateChangeRequest("PUBLIC");

            // when
            birthdayCafeService.changeVisibility(inProgressCafeId, HOST1, request);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, inProgressCafeId);
            assertThat(actual.getVisibility()).isEqualTo(Visibility.PUBLIC);
        }
    }

    @Nested
    @DisplayName("생일 카페 특전 등록은")
    class SpecialGoodsTest {

        private final List<SpecialGoodsRequest> request = List.of(
                new SpecialGoodsRequest("특전", "포토카드"),
                new SpecialGoodsRequest("디저트", "포토카드, ID 카드")
        );

        @Test
        void 진행_중인_카페에서_가능하다() {
            // when
            birthdayCafeService.replaceSpecialGoods(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, request);

            // then
            List<SpecialGoods> actual = entityManager.createQuery(
                            "select bc.specialGoods from BirthdayCafe bc where bc.id = :id", SpecialGoods.class)
                    .setParameter("id", IN_PROGRESS_BIRTHDAY_CAFE_ID)
                    .getResultList();
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new SpecialGoods("특전", "포토카드"),
                            new SpecialGoods("디저트", "포토카드, ID 카드")
                    ));
        }

        @Test
        void 기존_특전을_완전히_대체한다() {
            // given
            birthdayCafeService.replaceSpecialGoods(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, request);

            // when
            birthdayCafeService.replaceSpecialGoods(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, List.of(
                    new SpecialGoodsRequest("바뀐 특전", "새로운 포토카드"),
                    new SpecialGoodsRequest("바뀐 디저트", "새로운 포토카드, 새로운 ID 카드"),
                    new SpecialGoodsRequest("스페셜", "특별 선물")
            ));

            // then
            List<SpecialGoods> actual = entityManager.createQuery(
                            "select bc.specialGoods from BirthdayCafe bc where bc.id = :id", SpecialGoods.class)
                    .setParameter("id", IN_PROGRESS_BIRTHDAY_CAFE_ID)
                    .getResultList();
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new SpecialGoods("바뀐 특전", "새로운 포토카드"),
                            new SpecialGoods("바뀐 디저트", "새로운 포토카드, 새로운 ID 카드"),
                            new SpecialGoods("스페셜", "특별 선물")
                    ));
        }

        @Test
        void 대관_대기_상태에선_못한다() {
            // when then
            assertThatThrownBy(() -> birthdayCafeService.replaceSpecialGoods(PENDING_BIRTHDAY_CAFE_ID, HOST1, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자가_아니면_못한다() {
            // when then
            assertThatThrownBy(() -> birthdayCafeService.replaceSpecialGoods(IN_PROGRESS_BIRTHDAY_CAFE_ID, ANOTHER_MEMBER, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 메뉴 등록은")
    class BirthdayCafeMenuTest {

        private final List<MenuRequest> request = List.of(
                new MenuRequest("기본", "아메리카노+포토카드+ID카드", 10000),
                new MenuRequest("디저트", "케이크+포토카드+ID카드", 10000)
        );

        @Test
        void 진행_중인_카페에서_가능하다() {
            // when
            birthdayCafeService.replaceMenus(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, request);

            // then
            List<BirthdayCafeMenu> actual = entityManager.createQuery(
                            "select bc.birthdayCafeMenus from BirthdayCafe bc where bc.id = :id", BirthdayCafeMenu.class)
                    .setParameter("id", IN_PROGRESS_BIRTHDAY_CAFE_ID)
                    .getResultList();
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            BirthdayCafeMenu.of("기본", "아메리카노+포토카드+ID카드", 10000),
                            BirthdayCafeMenu.of("디저트", "케이크+포토카드+ID카드", 10000)

                    ));
        }

        @Test
        void 기존_메뉴를_완전히_대체한다() {
            // given
            birthdayCafeService.replaceMenus(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, request);

            // when
            birthdayCafeService.replaceMenus(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, List.of(
                    new MenuRequest("바뀐 기본 메뉴", "새로운 포토카드", 10000),
                    new MenuRequest("바뀐 디저트", "새로운 케이크, 새로운 ID 카드", 8000),
                    new MenuRequest("바뀐 메뉴", "새로운 ID 카드", 7500)
            ));

            // then
            List<BirthdayCafeMenu> actual = entityManager.createQuery(
                            "select bc.birthdayCafeMenus from BirthdayCafe bc where bc.id = :id", BirthdayCafeMenu.class)
                    .setParameter("id", IN_PROGRESS_BIRTHDAY_CAFE_ID)
                    .getResultList();
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new MenuRequest("바뀐 기본 메뉴", "새로운 포토카드", 10000),
                            new MenuRequest("바뀐 디저트", "새로운 케이크, 새로운 ID 카드", 8000),
                            new MenuRequest("바뀐 메뉴", "새로운 ID 카드", 7500)
                    ));
        }

        @Test
        void 대관_대기_상태에선_못한다() {
            // when then
            assertThatThrownBy(() -> birthdayCafeService.replaceMenus(PENDING_BIRTHDAY_CAFE_ID, HOST1, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자가_아니면_못한다() {
            // when then
            assertThatThrownBy(() -> birthdayCafeService.replaceMenus(IN_PROGRESS_BIRTHDAY_CAFE_ID, ANOTHER_MEMBER, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 럭키 드로우 등록은")
    class LuckyDrawTest {

        private final List<LuckyDrawRequest> requests = List.of(
                new LuckyDrawRequest(1, "머그컵"),
                new LuckyDrawRequest(2, "포토 카드")
        );

        @Test
        void 진행_중인_카페에서_가능하다() {
            // when
            birthdayCafeService.replaceLuckyDraws(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, requests);

            // then
            List<LuckyDraw> actual = entityManager.createQuery(
                            "select bc.luckyDraws from BirthdayCafe bc where bc.id = :id", LuckyDraw.class)
                    .setParameter("id", IN_PROGRESS_BIRTHDAY_CAFE_ID)
                    .getResultList();
            assertThat(actual)
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new LuckyDraw(1, "머그컵"),
                            new LuckyDraw(2, "포토 카드")

                    ));
        }

        @Test
        void 기존_럭키드로우를_완전히_대체한다() {
            // given
            birthdayCafeService.replaceLuckyDraws(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, requests);

            // when
            birthdayCafeService.replaceLuckyDraws(IN_PROGRESS_BIRTHDAY_CAFE_ID, HOST1, List.of());

            // then
            List<LuckyDraw> actual = entityManager.createQuery(
                            "select bc.luckyDraws from BirthdayCafe bc where bc.id = :id", LuckyDraw.class)
                    .setParameter("id", IN_PROGRESS_BIRTHDAY_CAFE_ID)
                    .getResultList();
            assertThat(actual).isEmpty();
        }

        @Test
        void 대관_대기_상태에선_못한다() {
            // when then
            assertThatThrownBy(() -> birthdayCafeService.replaceLuckyDraws(PENDING_BIRTHDAY_CAFE_ID, HOST1, requests))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자가_아니면_못한다() {
            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.replaceLuckyDraws(IN_PROGRESS_BIRTHDAY_CAFE_ID, ANOTHER_MEMBER, requests))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("생일 카페 정보 수정")
    class UpdateTest {

        @Test
        void 주최자만_가능하다() {
            // given
            String name = "BTS 생카";
            String twitterAccount = "@bts-birca";
            BirthdayCafeUpdateRequest request = new BirthdayCafeUpdateRequest(name, twitterAccount);

            // when
            birthdayCafeService.updateBirthdayCafe(PENDING_BIRTHDAY_CAFE_ID, HOST1, request);

            // then
            BirthdayCafe birthdayCafe = entityManager.find(BirthdayCafe.class, PENDING_BIRTHDAY_CAFE_ID);
            assertAll(
                    () -> assertThat(birthdayCafe.getName()).isEqualTo(name),
                    () -> assertThat(birthdayCafe.getTwitterAccount()).isEqualTo(twitterAccount)
            );
        }

        @Test
        void 주최자가_아니면_할_수_없다() {
            // given
            String name = "BTS 생카";
            String twitterAccount = "@bts-birca";
            BirthdayCafeUpdateRequest request = new BirthdayCafeUpdateRequest(name, twitterAccount);

            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.updateBirthdayCafe(PENDING_BIRTHDAY_CAFE_ID, ANOTHER_MEMBER, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }

    @Nested
    @DisplayName("카페 신청을 수락할 때")
    @Sql("/fixture/birthday-cafe-application-fixture.sql")
    class ApproveBirthdayCafeTest {

        private static final LoginMember CAFE_OWNER = new LoginMember(2L);

        @Test
        void 정상적으로_수락한다() {
            // given
            Long rentalPendingBirthdayCafeId = 4L;

            // when
            birthdayCafeService.approveBirthdayCafeApplication(rentalPendingBirthdayCafeId, CAFE_OWNER);
            ProgressState progressState = entityManager.createQuery(
                            "select bc.progressState from BirthdayCafe bc where bc.id = :id", ProgressState.class
                    )
                    .setParameter("id", rentalPendingBirthdayCafeId)
                    .getSingleResult();

            // then
            Assertions.assertThat(progressState).isEqualTo(ProgressState.RENTAL_APPROVED);
        }

        @Test
        void 존재하지_않는_생일_카페는_예외가_발생한다() {
            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.approveBirthdayCafeApplication(100L, CAFE_OWNER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }

        @Test
        void 이미_대관_완료된_날짜는_예외가_발생한다() {
            // given
            Long birthdayCafeId = 1L;

            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.approveBirthdayCafeApplication(birthdayCafeId, CAFE_OWNER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.RENTAL_ALREADY_EXISTS);
        }

        @Test
        void 진행_중인_날짜는_예외가_발생한다() {
            // given
            Long birthdayCafeId = 1L;

            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.approveBirthdayCafeApplication(birthdayCafeId, CAFE_OWNER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.RENTAL_ALREADY_EXISTS);
        }
    }

    @Nested
    @DisplayName("생일 카페 신청을 거절할 때")
    @Sql("/fixture/birthday-cafe-application-fixture.sql")
    class CancelBirthdayCafeApplicationTest {

        private static final LoginMember CAFE_OWNER = new LoginMember(2L);

        @Test
        void 정상적으로_거절한다() {
            // given
            Long birthdayCafeId = 1L;

            // when
            birthdayCafeService.cancelBirthdayCafeApplication(birthdayCafeId, CAFE_OWNER);
            ProgressState progressState = entityManager.createQuery(
                            "select bc.progressState from BirthdayCafe bc where bc.id = :id", ProgressState.class
                    )
                    .setParameter("id", birthdayCafeId)
                    .getSingleResult();

            // then
            assertThat(progressState).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 존재하지_않는_생일_카페는_예외가_발생한다() {
            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.cancelBirthdayCafeApplication(100L, CAFE_OWNER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @Sql("/fixture/birthday-cafe-schedule-fixture.sql")
    @DisplayName("카페 사장이 직접 생일 카페 일정을 추가할 때")
    class AddBirthDayCafeScheduleTest {

        @Test
        void 정상적으로_추가한다() {
            // given
            AddBirthdayCafeSchedule request = new AddBirthdayCafeSchedule(
                    ARTIST_ID,
                    LocalDateTime.of(2024, 5, 29, 0, 0, 0),
                    LocalDateTime.of(2024, 5, 30, 0, 0, 0),
                    100,
                    200,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when
            birthdayCafeService.addBirthdayCafeSchedule(request, CAFE_2_OWNER);
            BirthdayCafe actual = entityManager.createQuery(
                            "select bc from BirthdayCafe bc where bc.hostId is null", BirthdayCafe.class)
                    .getSingleResult();

            // then
            assertAll(
                    () -> assertThat(actual.getArtistId()).isEqualTo(request.artistId()),
                    () -> assertThat(actual.getHostId()).isNull(),
                    () -> assertThat(actual.getVisitants())
                            .isEqualTo(Visitants.of(request.minimumVisitant(), request.maximumVisitant())),
                    () -> assertThat(actual.getTwitterAccount()).isEqualTo(request.twitterAccount()),
                    () -> assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_APPROVED),
                    () -> assertThat(actual.getVisibility()).isEqualTo(Visibility.PRIVATE),
                    () -> assertThat(actual.getCongestionState()).isEqualTo(CongestionState.SMOOTH),
                    () -> assertThat(actual.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.ABUNDANT)
            );
        }

        @Test
        void 이미_대관된_날짜는_예외가_발생한다() {
            // given
            AddBirthdayCafeSchedule request = new AddBirthdayCafeSchedule(
                    ARTIST_ID,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    5,
                    10,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.addBirthdayCafeSchedule(request, CAFE_2_OWNER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.RENTAL_ALREADY_EXISTS);
        }

        @Test
        void 카페_휴무일이면_예외가_발생한다() {
            // given
            AddBirthdayCafeSchedule request = new AddBirthdayCafeSchedule(
                    ARTIST_ID,
                    LocalDateTime.of(2024, 3, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 3, 10, 0, 0, 0),
                    5,
                    10,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() ->
                    birthdayCafeService.addBirthdayCafeSchedule(request, CAFE_2_OWNER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(DayOffErrorCode.DAY_OFF_DATE);
        }

        @Test
        void 대관_일정_취소_후_다시_대관할_수_있다() {
            // given
            AddBirthdayCafeSchedule request = new AddBirthdayCafeSchedule(
                    ARTIST_ID,
                    LocalDateTime.of(2024, 7, 31, 0, 0, 0),
                    LocalDateTime.of(2024, 7, 31, 0, 0, 0),
                    100,
                    200,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when
            birthdayCafeService.addBirthdayCafeSchedule(request, CAFE_2_OWNER);
            BirthdayCafe actual = entityManager.createQuery(
                            "select bc from BirthdayCafe bc where bc.hostId is null", BirthdayCafe.class)
                    .getSingleResult();

            // then
            assertAll(
                    () -> assertThat(actual.getArtistId()).isEqualTo(request.artistId()),
                    () -> assertThat(actual.getHostId()).isNull(),
                    () -> assertThat(actual.getVisitants())
                            .isEqualTo(Visitants.of(request.minimumVisitant(), request.maximumVisitant())),
                    () -> assertThat(actual.getTwitterAccount()).isEqualTo(request.twitterAccount()),
                    () -> assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_APPROVED),
                    () -> assertThat(actual.getVisibility()).isEqualTo(Visibility.PRIVATE),
                    () -> assertThat(actual.getCongestionState()).isEqualTo(CongestionState.SMOOTH),
                    () -> assertThat(actual.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.ABUNDANT)
            );
        }
    }
}
