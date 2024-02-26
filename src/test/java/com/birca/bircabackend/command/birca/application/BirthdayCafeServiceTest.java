package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.*;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/birthday-cafe-fixture.sql")
class BirthdayCafeServiceTest extends ServiceTest {

    private static final LoginMember LOGIN_MEMBER = new LoginMember(1L);

    @Autowired
    private BirthdayCafeService birthdayCafeService;

    @PersistenceContext
    private EntityManager entityManager;


    @Nested
    @DisplayName("생일 카페를 위해 카페를 대관할 때")
    class ApplyRentalTest {

        private final int minimumVisitant = 5;
        private final int maximumVisitant = 10;
        private final LocalDateTime startDate = LocalDateTime.of(2024, 2, 8, 0, 0, 0);
        private final LocalDateTime endDate = LocalDateTime.of(2024, 2, 10, 0, 0, 0);
        private final ApplyRentalRequest validRequest = new ApplyRentalRequest(
                1L,
                1L,
                startDate,
                endDate,
                minimumVisitant,
                maximumVisitant,
                "@ChaseM",
                "010-0000-0000"
        );

        @Test
        void 정상적으로_신청한다() {
            // when
            birthdayCafeService.applyRental(validRequest, LOGIN_MEMBER);

            // then
            BirthdayCafe birthdayCafe = entityManager.find(BirthdayCafe.class, 1L);
            assertAll(
                    () -> assertThat(birthdayCafe.getArtistId()).isEqualTo(validRequest.artistId()),
                    () -> assertThat(birthdayCafe.getHostId()).isEqualTo(LOGIN_MEMBER.id()),
                    () -> assertThat(birthdayCafe.getSchedule()).isEqualTo(Schedule.of(startDate, endDate)),
                    () -> assertThat(birthdayCafe.getVisitants()).isEqualTo(Visitants.of(minimumVisitant, maximumVisitant)),
                    () -> assertThat(birthdayCafe.getTwitterAccount()).isEqualTo(validRequest.twitterAccount()),
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
                    1L,
                    1L,
                    startDate,
                    endDate,
                    5,
                    10,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, LOGIN_MEMBER))
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
                    1L,
                    1L,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    minimumVisitant,
                    maximumVisitant,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, LOGIN_MEMBER))
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
                    1L,
                    1L,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    minimumVisitant,
                    maximumVisitant,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }

        @Test
        void 이미_대관_대기_생일_카페가_있으면_또_대관할_수_없다() {
            // given
            birthdayCafeService.applyRental(validRequest, LOGIN_MEMBER);
            ApplyRentalRequest request = new ApplyRentalRequest(
                    1L,
                    2L,
                    LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                    LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                    minimumVisitant,
                    maximumVisitant,
                    "@ChaseM",
                    "010-0000-0000"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.applyRental(request, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.RENTAL_PENDING_EXISTS);
        }
    }
}
