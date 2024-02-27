package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeServiceTest extends ServiceTest {

    private static final LoginMember HOST = new LoginMember(1L);
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
            birthdayCafeService.applyRental(VALID_REQUEST, HOST);

            // then
            BirthdayCafe birthdayCafe = entityManager.find(BirthdayCafe.class, 1L);
            assertAll(
                    () -> assertThat(birthdayCafe.getArtistId()).isEqualTo(VALID_REQUEST.artistId()),
                    () -> assertThat(birthdayCafe.getHostId()).isEqualTo(HOST.id()),
                    () -> assertThat(birthdayCafe.getSchedule())
                            .isEqualTo(Schedule.of(VALID_REQUEST.startDate(), VALID_REQUEST.endDate())),
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
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST))
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
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST))
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
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }

        @Test
        void 이미_대관_대기_생일_카페가_있으면_또_대관할_수_없다() {
            // given
            birthdayCafeService.applyRental(VALID_REQUEST, HOST);
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
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, HOST))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.RENTAL_PENDING_EXISTS);
        }
    }

    @Nested
    @DisplayName("생일 카페 대관 신청을 취소할 때")
    class CancelRentalTest {

        private final Long birthdayCafeId = 1L;

        @BeforeEach
        void initBirthdayCafe() {
            birthdayCafeService.applyRental(VALID_REQUEST, HOST);
        }

        @Test
        void 주최자_취소한다() {
            // when
            birthdayCafeService.cancelRental(birthdayCafeId, HOST);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, birthdayCafeId);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 카페_사장님이_취소한다() {
            // when
            birthdayCafeService.cancelRental(birthdayCafeId, CAFE_1_OWNER);

            // then
            BirthdayCafe actual = entityManager.find(BirthdayCafe.class, birthdayCafeId);
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_CANCELED);
        }

        @Test
        void 대관_대기_상태가_아니면_예외가_발생한다() {
            // given
            birthdayCafeService.cancelRental(birthdayCafeId, HOST);

            // when then
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(birthdayCafeId, HOST))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_CANCEL_RENTAL);
        }

        @Test
        void 없는_생일_카페면_예외가_발생한다() {
            // given
            long notExistCafe = 100L;

            // when then
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(notExistCafe, HOST))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }

        @Test
        void 생일_카페_주최자_또는_카페_사장님이_아니면_취소할_수_없다() {
            // when // than
            assertThatThrownBy(() -> birthdayCafeService.cancelRental(birthdayCafeId, ANOTHER_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.UNAUTHORIZED_HOST);
        }
    }
}
