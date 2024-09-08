package com.birca.bircabackend.command.birca.domain.value;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class VisitantsTest {

    @Nested
    @DisplayName("생일 카페 방문자 생성 시")
    class OfTest {

        @ParameterizedTest
        @CsvSource({"1, 10", "1, 2", "7, 8"})
        void 최소_방문자보다_최대_방문자가_커야_한다(Integer minimumVisitant, Integer maximumVisitant) {
            // when then
            assertThat(Visitants.of(minimumVisitant, maximumVisitant)).isNotNull();
        }

        @ParameterizedTest
        @CsvSource({"0, 1", "1, 0", "-1, 2", "1, -2", "-5, -1", "0, 0"})
        void 방문자_수는_자연수여야_한다(Integer minimumVisitant, Integer maximumVisitant) {
            // when then
            assertThatThrownBy(() -> Visitants.of(minimumVisitant, maximumVisitant))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }

        @ParameterizedTest
        @CsvSource({"2, 1", "10, 1", "8, 7"})
        void 최소_방문자_보다_최대_방문자가_커야_한다(Integer minimumVisitant, Integer maximumVisitant) {
            // when then
            assertThatThrownBy(() -> Visitants.of(minimumVisitant, maximumVisitant))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }

        @Test
        void 최소_방문자가_null이어도_생성된다() {
            // given
            Integer minimumVisitant = null;
            Integer maximumVisitant = 10;

            // when then
            assertThat(Visitants.of(minimumVisitant, maximumVisitant)).isNotNull();
        }

        @Test
        void 최대_방문자가_null이어도_생성된다() {
            // given
            Integer minimumVisitant = 10;
            Integer maximumVisitant = null;

            // when then
            assertThat(Visitants.of(minimumVisitant, maximumVisitant)).isNotNull();
        }
    }
}
