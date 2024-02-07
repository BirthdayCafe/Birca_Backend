package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.Schedule;
import com.birca.bircabackend.command.birca.domain.Visitants;
import com.birca.bircabackend.command.birca.dto.BirthdayCafeCreateRequest;
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
    @DisplayName("생일 카페 생성 시")
    class CreateTest {

        @Test
        void 정상적으로_생성한다() {
            // given
            int minimumVisitant = 5;
            int maximumVisitant = 10;
            LocalDateTime startDate = LocalDateTime.of(2024, 2, 8, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 2, 10, 0, 0, 0);
            BirthdayCafeCreateRequest request = new BirthdayCafeCreateRequest(
                    1L,
                    startDate,
                    endDate,
                    minimumVisitant,
                    maximumVisitant,
                    "@ChaseM"
            );

            // when
            birthdayCafeService.createBirthdayCafe(request, LOGIN_MEMBER);

            // then
            BirthdayCafe birthdayCafe = entityManager.find(BirthdayCafe.class, 1L);
            assertAll(
                    () -> assertThat(birthdayCafe.getArtistId()).isEqualTo(request.artistId()),
                    () -> assertThat(birthdayCafe.getHostId()).isEqualTo(LOGIN_MEMBER.id()),
                    () -> assertThat(birthdayCafe.getSchedule()).isEqualTo(Schedule.of(startDate, endDate)),
                    () -> assertThat(birthdayCafe.getVisitants()).isEqualTo(Visitants.of(minimumVisitant, maximumVisitant)),
                    () -> assertThat(birthdayCafe.getTwitterAccount()).isEqualTo(request.twitterAccount())
            );
        }

        @Test
        void 아티스트와_호스트만_있어도_생성_가능하다() {
            // given
            BirthdayCafeCreateRequest request = new BirthdayCafeCreateRequest(
                    1L,
                    null,
                    null,
                    null,
                    null,
                    null
            );

            // when
            birthdayCafeService.createBirthdayCafe(request, LOGIN_MEMBER);

            // then
            BirthdayCafe birthdayCafe = entityManager.find(BirthdayCafe.class, 1L);
            assertAll(
                    () -> assertThat(birthdayCafe.getArtistId()).isEqualTo(request.artistId()),
                    () -> assertThat(birthdayCafe.getHostId()).isEqualTo(LOGIN_MEMBER.id()),
                    () -> assertThat(birthdayCafe.getSchedule()).isNull(),
                    () -> assertThat(birthdayCafe.getVisitants()).isNull(),
                    () -> assertThat(birthdayCafe.getTwitterAccount()).isNull()
            );
        }

        @Test
        void 시작일이_종료일보다_앞일_수_없다() {
            // given
            LocalDateTime startDate = LocalDateTime.of(2024, 2, 11, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 2, 10, 0, 0, 0);
            BirthdayCafeCreateRequest request = new BirthdayCafeCreateRequest(
                    1L,
                    startDate,
                    endDate,
                    5,
                    10,
                    "@ChaseM"
            );

            // when then
            assertThatThrownBy(() -> birthdayCafeService.createBirthdayCafe(request, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_SCHEDULE);
        }
    }

    @Test
    void 최소_방문자는_최대_방문자_보다_클_수_없다() {
        // given
        int minimumVisitant = 11;
        int maximumVisitant = 10;
        BirthdayCafeCreateRequest request = new BirthdayCafeCreateRequest(
                1L,
                LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                minimumVisitant,
                maximumVisitant,
                "@ChaseM"
        );

        // when then
        assertThatThrownBy(() -> birthdayCafeService.createBirthdayCafe(request, LOGIN_MEMBER))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
    }

    @Test
    void 최소_방문자와_최대_방문자는_양수여야_한다() {
        // given
        int minimumVisitant = -1;
        int maximumVisitant = -1;
        BirthdayCafeCreateRequest request = new BirthdayCafeCreateRequest(
                1L,
                LocalDateTime.of(2024, 2, 8, 0, 0, 0),
                LocalDateTime.of(2024, 2, 10, 0, 0, 0),
                minimumVisitant,
                maximumVisitant,
                "@ChaseM"
        );

        // when then
        assertThatThrownBy(() -> birthdayCafeService.createBirthdayCafe(request, LOGIN_MEMBER))
                .isInstanceOf(BusinessException.class)
                .extracting("errorCode")
                .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
    }
}
