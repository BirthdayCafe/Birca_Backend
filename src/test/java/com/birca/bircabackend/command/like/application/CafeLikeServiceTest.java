package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.command.like.exception.LikeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/cafe-fixture.sql")
class CafeLikeServiceTest extends ServiceTest {

    @Autowired
    private CafeLikeService cafeLikeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("카페를 찜할 때")
    class LikeCafeTest {

        private static final LoginMember LOGIN_MEMBER = new LoginMember(1L);

        @Test
        void 정상적으로_찜한다() {
            // given
            Long cafeId = 1L;

            // when
            cafeLikeService.like(cafeId, LOGIN_MEMBER);
            Like cafeLike = entityManager.find(Like.class, cafeId);

            // then
            Assertions.assertThat(cafeLike).isNotNull();
        }

        @Test
        void 존재하지_않는_카페는_예외가_발생한다() {
            // given
            Long cafeId = 100L;

            // when then
            assertThatThrownBy(() -> cafeLikeService.like(cafeId, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(CafeErrorCode.NOT_FOUND);
        }

        @Test
        void 사업자등록증이_승인되지_않은_카페는_예외가_발생한다() {
            // given
            Long cafeId = 2L;

            // when then
            assertThatThrownBy(() -> cafeLikeService.like(cafeId, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_CAFE_LIKE);
        }
    }
}
