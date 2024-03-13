package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import com.birca.bircabackend.query.dto.SpecialGoodsResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/birthday-cafe-special-goods-fixture.sql")
class BirthdayCafeQueryServiceTest extends ServiceTest {

    @Autowired
    private BirthdayCafeQueryService birthdayCafeQueryService;

    @Test
    void 생일_카페_특전_목록을_조회한다() {
        // given
        Long birthdayCafeId = 1L;

        // when
        List<SpecialGoodsResponse> actual = birthdayCafeQueryService.findSpecialGoods(birthdayCafeId);

        // then
        assertThat(actual)
                .containsOnly(
                        new SpecialGoodsResponse("특전", "포토카드"),
                        new SpecialGoodsResponse("디저트", "포토카드, ID 카드")
                );
    }
}
