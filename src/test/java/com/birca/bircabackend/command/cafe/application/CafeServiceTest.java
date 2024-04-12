package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/cafe-fixture.sql")
class CafeServiceTest extends ServiceTest {

    @Autowired
    private CafeService cafeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("카페 상세 정보를 수정할 때")
    class UpdateCafeTest {

        private static final CafeUpdateRequest REQUEST = new CafeUpdateRequest(
                "메가커피",
                "서울특별시 강남구 테헤란로 212",
                "@ChaseM",
                "8시 - 22시",
                List.of(new CafeUpdateRequest.CafeMenuResponse("바닐라 라떼", 2500)),
                List.of(new CafeUpdateRequest.CafeOptionResponse("앨범", 20000))
        );

        @Test
        void 정상적으로_수정한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);

            // when
            cafeService.update(loginMember, REQUEST);
            Cafe cafeResponse = entityManager.createQuery("select c from Cafe c where c.ownerId = :ownerId", Cafe.class)
                    .setParameter("ownerId", loginMember.id())
                    .getSingleResult();

            List<CafeMenu> cafeMenusResponse = entityManager.createQuery("select c.cafeMenus from Cafe c where c.ownerId = :ownerId", CafeMenu.class)
                    .setParameter("ownerId", loginMember.id())
                    .getResultList();

            List<CafeOption> cafeOptionResponse = entityManager.createQuery("select c.cafeOptions from Cafe c where c.ownerId = :ownerId", CafeOption.class)
                    .setParameter("ownerId", loginMember.id())
                    .getResultList();

            // then
            assertAll(
                    () -> assertThat(cafeResponse.getName()).isEqualTo("메가커피"),
                    () -> assertThat(cafeResponse.getAddress()).isEqualTo("서울특별시 강남구 테헤란로 212"),
                    () -> assertThat(cafeResponse.getTwitterAccount()).isEqualTo("@ChaseM"),
                    () -> assertThat(cafeResponse.getBusinessHours()).isEqualTo("8시 - 22시"),
                    () -> assertThat(cafeMenusResponse)
                            .usingRecursiveComparison()
                            .isEqualTo(List.of(new CafeMenu("바닐라 라떼", 2500))),
                    () -> assertThat(cafeOptionResponse)
                            .usingRecursiveComparison()
                            .isEqualTo(List.of(new CafeOption("앨범", 20000)))
            );
        }

        @Test
        void 존재하지_않는_카페는_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(100L);

            // when then
            assertThatThrownBy(() -> cafeService.update(loginMember, REQUEST))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }
    }
}
