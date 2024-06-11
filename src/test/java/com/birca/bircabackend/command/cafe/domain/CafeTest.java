package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class CafeTest {

    private static final Long OWNER_ID = 1L;
    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .build();
    private static final Cafe CAFE = fixtureMonkey.giveMeBuilder(Cafe.class)
            .set("ownerId", 1L)
            .sample();

    @Nested
    @DisplayName("카페 정보를 수정할 때")
    class UpdateCafeTest {

        @Test
        void 자신의_카페가_아니면_예외가_발생한다() {
            // when then
            assertThatThrownBy(() -> CAFE.update(100L, "메가커피", "서울특별시 강남구 테헤란로 212",
                    "@ChaseM", "8시 - 22시"))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.UNAUTHORIZED_UPDATE);
        }

        @Test
        void 일반_정보를_수정한다() {
            // when
            CAFE.update(OWNER_ID, "메가커피", "서울특별시 강남구 테헤란로 212",
                    "@ChaseM", "8시 - 22시");

            // then
            assertAll(
                    () -> assertThat(CAFE.getName()).isEqualTo("메가커피"),
                    () -> assertThat(CAFE.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 212"),
                    () -> assertThat(CAFE.getTwitterAccount()).isEqualTo("@ChaseM"),
                    () -> assertThat(CAFE.getBusinessHours()).isEqualTo("8시 - 22시")
            );
        }

        @Test
        void 메뉴를_수정한다() {
            // when
            CAFE.updateCafeMenus(OWNER_ID, List.of(new CafeMenu("아메리카노", 1500)));

            // then
            assertThat(CAFE.getCafeMenus())
                    .containsOnly(new CafeMenu("아메리카노", 1500));
        }

        @Test
        void 옵션을_수정한다() {
            // when
            CAFE.replaceCafeOptions(OWNER_ID, List.of(new CafeOption("액자", 2000)));

            // then
            assertThat(CAFE.getCafeOptions())
                    .containsOnly(new CafeOption("액자", 2000));
        }
    }

    @Nested
    @DisplayName("카페 휴무일을 설정할 때")
    class MarkDayOffTest {

        @Test
        void 정상적으로_설정한다() {
            // when then
            assertDoesNotThrow(() -> CAFE.markDayOff(OWNER_ID, LocalDateTime.now()));
        }

        @Test
        void 자신의_카페가_아니면_예외가_발생한다() {
            // when then
            assertThatThrownBy(() -> CAFE.markDayOff(100L, LocalDateTime.now()))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.UNAUTHORIZED_UPDATE);
        }
    }
}
