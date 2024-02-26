package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class BirthdayCafeTest {

    @Nested
    @DisplayName("생일 카페는 카페 대관 신청 시")
    class ApplyRentalTest {

        private final Long hostId = 1L;
        private final Long artistId = 1L;
        private final Long cafeId = 1L;
        private final Schedule schedule = Schedule.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1));
        private final Visitants visitants = Visitants.of(3, 10);
        private final String twitterAccount = "ChaseM";
        private final PhoneNumber hostPhoneNumber = PhoneNumber.from("010-0000-0000");


        @Test
        void 대관_대기_상태로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    hostId, artistId, cafeId, schedule, visitants, twitterAccount, hostPhoneNumber);

            // then
            assertThat(actual.getProgressState()).isEqualTo(ProgressState.RENTAL_PENDING);
        }

        @Test
        void 공개_상태가_비공개로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    hostId, artistId, cafeId, schedule, visitants, twitterAccount, hostPhoneNumber);

            // then
            assertThat(actual.getVisibility()).isEqualTo(Visibility.PRIVATE);
        }

        @Test
        void 혼잡도가_원할로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    hostId, artistId, cafeId, schedule, visitants, twitterAccount, hostPhoneNumber);

            // then
            assertThat(actual.getCongestionState()).isEqualTo(CongestionState.SMOOTH);
        }

        @Test
        void 특전_재고는_많음으로_생성된다() {
            // when
            BirthdayCafe actual = BirthdayCafe.applyRental(
                    hostId, artistId, cafeId, schedule, visitants, twitterAccount, hostPhoneNumber);

            // then
            assertThat(actual.getSpecialGoodsStockState()).isEqualTo(SpecialGoodsStockState.ABUNDANT);
        }
    }
}
