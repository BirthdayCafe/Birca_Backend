package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/cafe-fixture.sql")
class CafeQueryServiceTest extends ServiceTest {

    @Autowired
    private CafeQueryService cafeQueryService;

    @Nested
    @DisplayName("카페 검색 시")
    class searchCafeTest {

        @Test
        void 카페_전체_이름으로_검색한다() {
            // given
            String name = "우지커피";

            // when
            List<CafeResponse> actual = cafeQueryService.findCafes(name);

            // then
            assertThat(actual).isEqualTo(List.of(new CafeResponse(2L, "우지커피")));
        }

        @Test
        void 카페_일부_이름으로_검색한다() {
            // given
            String name = "커피";

            // when
            List<CafeResponse> actual = cafeQueryService.findCafes(name);

            // then
            assertThat(actual)
                    .containsOnly(
                            new CafeResponse(2L, "우지커피"),
                            new CafeResponse(3L, "메가커피")
                    );
        }
    }

}
