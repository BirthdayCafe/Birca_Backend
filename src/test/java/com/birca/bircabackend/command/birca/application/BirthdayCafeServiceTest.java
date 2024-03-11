package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.dto.SpecialGoodsRequest;
import com.birca.bircabackend.command.birca.dto.StateChangeRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
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
class BirthdayCafeServiceTest extends ServiceTest {

    private static final LoginMember HOST1 = new LoginMember(1L);
    private static final LoginMember HOST2 = new LoginMember(2L);
    private static final LoginMember ANOTHER_MEMBER = new LoginMember(2L);
    private static final LoginMember CAFE_1_OWNER = new LoginMember(3L);
    private static final long ARTIST_ID = 1L;
    private static final long CAFE1_ID = 1L;
    private static final long CAFE2_ID = 2L;

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

        private final Long rentalPendingCafeId = 1L;

        @Test
        void 주최자_취소한다() {
            // when
            birthdayCafeService.cancelRental(rentalPendingCafeId, HOST1);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, rentalPendingCafeId);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 카페_사장님이_취소한다() {
            // when
            birthdayCafeService.cancelRental(rentalPendingCafeId, CAFE_1_OWNER);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, rentalPendingCafeId);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 대관_대기_상태가_아니면_예외가_발생한다() {
            // given
            birthdayCafeService.cancelRental(rentalPendingCafeId, HOST1);

            // when then
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(rentalPendingCafeId, HOST1))
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
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(rentalPendingCafeId, ANOTHER_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_CANCEL);
        }
    }

    @Nested
    @DisplayName("생일 카페 상태에서")
    class ChangeStateTest {
        private final Long inProgressCafeId = 2L;

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

        private final Long rentalPendingCafeId = 1L;
        private final Long inProgressCafeId = 2L;

        private final List<SpecialGoodsRequest> request = List.of(
                new SpecialGoodsRequest("특전", "포토카드"),
                new SpecialGoodsRequest("디저트", "포토카드, ID 카드")
        );

        @Test
        void 진행_중인_카페에서_가능하다() {
            // when
            birthdayCafeService.replaceSpecialGoods(inProgressCafeId, HOST1, request);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, inProgressCafeId);
            assertThat(actual.getSpecialGoods())
                    .usingRecursiveComparison()
                    .isEqualTo(List.of(
                            new SpecialGoods("특전", "포토카드"),
                            new SpecialGoods("디저트", "포토카드, ID 카드")
                    ));
        }

        @Test
        void 기존_특전을_완전히_대체한다() {
            // given
            birthdayCafeService.replaceSpecialGoods(inProgressCafeId, HOST1, request);

            // when
            birthdayCafeService.replaceSpecialGoods(inProgressCafeId, HOST1, List.of(
                    new SpecialGoodsRequest("바뀐 특전", "새로운 포토카드"),
                    new SpecialGoodsRequest("바뀐 디저트", "새로운 포토카드, 새로운 ID 카드"),
                    new SpecialGoodsRequest("스페셜", "특별 선물")
            ));

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, inProgressCafeId);
            assertThat(actual.getSpecialGoods())
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
            assertThatThrownBy(() -> birthdayCafeService.replaceSpecialGoods(rentalPendingCafeId, HOST1, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_UPDATE);
        }

        @Test
        void 주최자가_아니면_못한다() {
            // when then
            assertThatThrownBy(() -> birthdayCafeService.replaceSpecialGoods(inProgressCafeId, ANOTHER_MEMBER, request))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }
}
