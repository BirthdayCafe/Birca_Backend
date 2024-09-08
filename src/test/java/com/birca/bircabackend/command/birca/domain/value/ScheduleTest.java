package com.birca.bircabackend.command.birca.domain.value;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class ScheduleTest {

    @Nested
    @DisplayName("생일 카페 스케줄 생성 시")
    class OfTest {

        @ParameterizedTest
        @CsvSource({"2024, 2, 6", "2024, 3, 6", "2024, 2, 7", "2025, 2, 6"})
        void 시작일이_종료일_보다_과거면_생성_가능하다(int endDateYear, int endDateMonth, int endDateDay) {
            // given
            LocalDateTime startDate = LocalDateTime.of(2024, 2, 6, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(endDateYear, endDateMonth, endDateDay, 0, 0, 0);

            // when then
            assertThat(Schedule.of(startDate, endDate)).isNotNull();
        }

        @ParameterizedTest
        @CsvSource({"2024, 2, 5", "2024, 1, 6", "2023, 2, 6"})
        void 시작일이_종료일_보다_미래일_수_없다(int endDateYear, int endDateMonth, int endDateDay) {
            // given
            LocalDateTime startDate = LocalDateTime.of(2024, 2, 6, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(endDateYear, endDateMonth, endDateDay, 0, 0, 0);

            // when then
            assertThatThrownBy(() -> Schedule.of(startDate, endDate))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_SCHEDULE);
        }
    }
}
