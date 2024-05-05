package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.command.like.exception.LikeErrorCode;
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

@Sql("/fixture/cafe-fixture.sql")
class CafeLikeServiceTest extends ServiceTest {

    @Autowired
    private CafeLikeService cafeLikeService;

    @PersistenceContext
    private EntityManager entityManager;

    private static final LoginMember LOGIN_MEMBER = new LoginMember(1L);

    @Nested
    @DisplayName("카페를 찜할 때")
    class LikeCafeTest {

        @Test
        void 정상적으로_찜한다() {
            // given
            Long cafeId = 1L;

            // when
            cafeLikeService.like(cafeId, LOGIN_MEMBER);
            Like cafeLike = entityManager.find(Like.class, cafeId);

            // then
            assertThat(cafeLike).isNotNull();
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

    @Nested
    @DisplayName("찜한 카페를 취소할 때")
    class CancelCafeLikeTest {

        @Test
        void 정상적으로_취소한다() {
            // given
            Long cafeId = 2L;

            // when
            cafeLikeService.cancelLike(cafeId, LOGIN_MEMBER);
            List<Like> likes = entityManager.createQuery("select l from Like l where l.target.targetType = 'CAFE' and l.visitantId = :visitantId",
                            Like.class)
                    .setParameter("visitantId", LOGIN_MEMBER.id())
                    .getResultList();

            // then
            assertThat(likes.size()).isEqualTo(1);
        }

        @Test
        void 찜하지_않은_카페는_예외가_발생한다() {
            // given
            Long cafeId = 1L;

            // when then
            assertThatThrownBy(() -> cafeLikeService.cancelLike(cafeId, LOGIN_MEMBER))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_CANCEL);
        }
    }
}
