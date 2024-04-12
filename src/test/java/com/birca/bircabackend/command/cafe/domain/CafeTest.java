package com.birca.bircabackend.command.cafe.domain;

import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class CafeTest {

    @Nested
    @DisplayName("카페 정보를 수정할 때")
    class UpdateCafeTest {

        private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
                .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
                .build();

        @Test
        void 일반_정보를_수정한다() {
            // given
            Cafe cafe = fixtureMonkey.giveMeBuilder(Cafe.class)
                    .sample();

            // when
            cafe.update("메가커피", "서울특별시 강남구 테헤란로 212",
                    "@ChaseM", "8시 - 22시");

            // then
            assertAll(
                    () -> assertThat(cafe.getName()).isEqualTo("메가커피"),
                    () -> assertThat(cafe.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 212"),
                    () -> assertThat(cafe.getTwitterAccount()).isEqualTo("@ChaseM"),
                    () -> assertThat(cafe.getBusinessHours()).isEqualTo("8시 - 22시")
            );
        }

        @Test
        void 메뉴를_수정한다() {
            // given
            Cafe cafe = fixtureMonkey.giveMeBuilder(Cafe.class)
                    .sample();

            // when
            cafe.replaceCafeMenus(List.of(new CafeMenu("아메리카노", 1500)));

            // then
            assertThat(cafe.getCafeMenus())
                    .containsOnly(new CafeMenu("아메리카노", 1500));
        }

        @Test
        void 옵션을_수정한다() {
            // given
            Cafe cafe = fixtureMonkey.giveMeBuilder(Cafe.class)
                    .sample();

            // when
            cafe.replaceCafeOptions(List.of(new CafeOption("액자", 2000)));

            // then
            assertThat(cafe.getCafeOptions())
                    .containsOnly(new CafeOption("액자", 2000));
        }
    }
}
