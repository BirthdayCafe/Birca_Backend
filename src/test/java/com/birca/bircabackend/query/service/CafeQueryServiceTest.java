package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/cafe-fixture.sql")
class CafeQueryServiceTest extends ServiceTest {

    @Autowired
    private CafeQueryService cafeQueryService;

    @Test
    void 카페를_검색한다() {
        // given
        String name = "커피";

        // when
        List<CafeResponse> actual = cafeQueryService.findCafes(name);

        // then
        assertThat(actual)
                .containsOnly(
                        new CafeResponse(2L, "우지 커피"),
                        new CafeResponse(3L, "메가 커피")
                );
    }
}
