package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/cafe-fixture.sql")
class CafeQueryServiceTest extends ServiceTest {

    @Autowired
    private CafeQueryService cafeQueryService;

    @Nested
    @DisplayName("사장님이 카페 상세 조회할 때")
    class FindMyCafeDetailTest {

        @Test
        void 정상적으로_조회한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);

            // when
            MyCafeDetailResponse actual = cafeQueryService.findMyCafeDetails(loginMember);

            // then
            assertThat(actual).isEqualTo(new MyCafeDetailResponse(
                    1L,
                    "미스티우드",
                    "경기도 시흥시 은계중앙로 115",
                    "@ChaseM",
                    "6시 - 22시",
                    List.of(new MyCafeDetailResponse.CafeMenuResponse("아메리카노", 1500)),
                    List.of(new MyCafeDetailResponse.CafeOptionResponse("액자", 2000)),
                    List.of("image1.com", "image2.com", "image3.com", "image4.com", "image5.com")
            ));
        }

        @Test
        void 존재하지_않는_카페는_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(100L);

            // when then
            assertThatThrownBy(() -> cafeQueryService.findMyCafeDetails(loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("주최자가 대관 가능한 카페 목록을")
    class GetRentalAvailableCafe {

        private static final LoginMember LOGIN_MEMBER = new LoginMember(1L);

        @Test
        void 전부_조회한다() {
            // given
            CafeParams cafeParams = new CafeParams();
            PagingParams pagingParams = new PagingParams();

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsExactly(
                            new CafeSearchResponse(1L, "미스티우드", false, "image1.com", "@ChaseM", "경기도 시흥시 은계중앙로 115"),
                            new CafeSearchResponse(2L, "우지커피", true, "image6.com", "@ChaseM", "경기도 성남시 분당구 판교역로 235"),
                            new CafeSearchResponse(3L, "메가커피", true, "image7.com", "@ChaseM", "서울특별시 강남구 테헤란로 212")
                    );
        }

        @Test
        void 커서_이후로_조회한다() {
            // given
            CafeParams cafeParams = new CafeParams();
            PagingParams pagingParams = new PagingParams();
            pagingParams.setCursor(2L);

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsExactly(
                            new CafeSearchResponse(3L, "메가커피", true, "image7.com", "@ChaseM", "서울특별시 강남구 테헤란로 212")
                    );
        }

        @Test
        void 카페_전체_이름으로_조회한다() {
            // given
            CafeParams cafeParams = new CafeParams();
            cafeParams.setName("미스티우드");
            PagingParams pagingParams = new PagingParams();

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsOnly(
                            new CafeSearchResponse(1L, "미스티우드", false, "image1.com", "@ChaseM", "경기도 시흥시 은계중앙로 115")
                    );
        }

        @Test
        void 카페_일부_이름으로_조회한다() {
            // given
            CafeParams cafeParams = new CafeParams();
            cafeParams.setName("미스티");
            PagingParams pagingParams = new PagingParams();

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsOnly(
                            new CafeSearchResponse(1L, "미스티우드", false, "image1.com", "@ChaseM", "경기도 시흥시 은계중앙로 115")
                    );
        }

        @Test
        void 대관_가능한_날짜로_조회할_때_휴무일이면_제외된다() {
            // given
            CafeParams cafeParams = new CafeParams();
            LocalDateTime startDate = LocalDateTime.of(2024, 2, 17, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 2, 18, 0, 0, 0);
            cafeParams.setStartDate(startDate);
            cafeParams.setEndDate(endDate);

            PagingParams pagingParams = new PagingParams();

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsOnly(
                            new CafeSearchResponse(1L, "미스티우드", false, "image1.com", "@ChaseM", "경기도 시흥시 은계중앙로 115"),
                            new CafeSearchResponse(2L, "우지커피", true, "image6.com", "@ChaseM", "경기도 성남시 분당구 판교역로 235")

                    );
        }

        @Test
        void 대관_가능한_날짜로_조회할_때_대관된_날짜면_제외된다() {
            // given
            CafeParams cafeParams = new CafeParams();
            LocalDateTime startDate = LocalDateTime.of(2024, 3, 15, 0, 0, 0);
            LocalDateTime endDate = LocalDateTime.of(2024, 3, 16, 0, 0, 0);
            cafeParams.setStartDate(startDate);
            cafeParams.setEndDate(endDate);

            PagingParams pagingParams = new PagingParams();

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsOnly(
                            new CafeSearchResponse(2L, "우지커피", true, "image6.com", "@ChaseM", "경기도 성남시 분당구 판교역로 235"),
                            new CafeSearchResponse(3L, "메가커피", true, "image7.com", "@ChaseM", "서울특별시 강남구 테헤란로 212")
                    );
        }

        @Test
        void 찜한_카페들만_조회한다() {
            // given
            CafeParams cafeParams = new CafeParams();
            cafeParams.setLiked(true);

            PagingParams pagingParams = new PagingParams();

            // when
            List<CafeSearchResponse> actual = cafeQueryService.searchRentalAvailableCafes(LOGIN_MEMBER, cafeParams, pagingParams);

            // then
            assertThat(actual)
                    .containsExactly(
                            new CafeSearchResponse(2L, "우지커피", true, "image6.com", "@ChaseM", "경기도 성남시 분당구 판교역로 235"),
                            new CafeSearchResponse(3L, "메가커피", true, "image7.com", "@ChaseM", "서울특별시 강남구 테헤란로 212")
                    );
        }
    }

    @Nested
    @DisplayName("주최자가 카페 상세를")
    class FindCafeDetailTest {

        @Test
        void 조회한다() {
            // given
            Long cafeId = 1L;
            LoginMember loginMember = new LoginMember(1L);

            // when
            CafeDetailResponse actual = cafeQueryService.findCafeDetail(loginMember, cafeId);

            // then
            assertThat(actual).isEqualTo(
                    new CafeDetailResponse(true, "미스티우드", "@ChaseM",
                            "경기도 시흥시 은계중앙로 115", "6시 - 22시",
                            List.of("image1.com", "image2.com", "image3.com", "image4.com", "image5.com"),
                            List.of(
                                    new CafeDetailResponse.CafeMenuResponse(
                                            "아메리카노", 1500
                                    )
                            ),
                            List.of(
                                    new CafeDetailResponse.CafeOptionResponse(
                                            "액자", 2000
                                    )
                            )
                    ));
        }
    }

    @Nested
    @DisplayName("카페 대관된 날짜를 조회할 때")
    class FindCafeRentalDates {

        private static final DateParams DATE_PARAMS = new DateParams();

        @Test
        void 정상적으로_조회한다() {
            // given
            Long cafeId = 1L;
            DATE_PARAMS.setYear(2024);
            DATE_PARAMS.setMonth(3);

            // when
            List<CafeRentalDateResponse> actual = cafeQueryService.findCafeRentalDates(cafeId, DATE_PARAMS);

            // then
            assertThat(actual)
                    .containsExactly(
                            new CafeRentalDateResponse(2024, 3, 15, 2024, 3, 16),
                            new CafeRentalDateResponse(2024, 3, 20, 2024, 3, 20)
                    );
        }

        @Test
        void 존재하지_않는_카페는_예외가_발생한다() {
            // given
            Long cafeId = 100L;
            DATE_PARAMS.setYear(2024);
            DATE_PARAMS.setMonth(3);

            // when then
            assertThatThrownBy(() -> cafeQueryService.findCafeRentalDates(cafeId, DATE_PARAMS))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }
}
