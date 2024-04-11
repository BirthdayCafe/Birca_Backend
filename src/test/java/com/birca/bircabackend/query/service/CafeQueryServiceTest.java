package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.CafeDetailResponse;
import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Nested
    @DisplayName("사장님이 카페 상세 조회할 때")
    class FindCafeDetailTest {

        @Test
        void 정상적으로_조회한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);

            // when
            CafeDetailResponse actual = cafeQueryService.findCafeDetail(loginMember);

            // then
            assertThat(actual).isEqualTo(new CafeDetailResponse(
                    "미스티우드",
                    "경기도 시흥시 은계중앙로 115",
                    "@ChaseM",
                    "6시 - 22시",
                    List.of(new CafeDetailResponse.CafeMenuResponse("아메리카노", 1500)),
                    List.of(new CafeDetailResponse.CafeOptionResponse("액자", 2000)),
                    List.of("image1.com", "image2.com", "image3.com", "image4.com", "image5.com")
            ));
        }

        @Test
        void 존재하지_않는_카페는_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(100L);

            // when then
            assertThatThrownBy(() -> cafeQueryService.findCafeDetail(loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }
}
