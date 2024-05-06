package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

@Sql("/fixture/birthday-cafe-like-fixture.sql")
class BirthdayCafeLikeServiceTest extends ServiceTest {

    @Autowired
    private BirthdayCafeLikeService birthdayCafeLikeService;

    @PersistenceContext
    private EntityManager entityManager;

    @Nested
    @DisplayName("생일카페 찜하기를")
    class BirthdayCafeLikeTest {

        @Test
        void 정상적으로_성공한다() {
            // given
            Long birthdayCafeId = 1L;
            LoginMember loginMember = new LoginMember(1L);

            // when
            birthdayCafeLikeService.like(birthdayCafeId, loginMember);

            // then
            Like birthdayCafeLike = entityManager.find(Like.class, 1L);
            assertThat(birthdayCafeLike.getTarget().getTargetId()).isEqualTo(1L);
            assertThat(birthdayCafeLike.getVisitantId()).isEqualTo(1L);
        }

        @Test
        void 대관_대기_일_때는_할_수_없다() {
            // given
            Long birthdayCafeId = 2L;
            LoginMember loginMember = new LoginMember(1L);

            // when then
            assertThatThrownBy(() -> birthdayCafeLikeService.like(birthdayCafeId, loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_BIRTHDAY_CAFE_LIKE);
        }

        @Test
        void 취소_상태_일_때는_할_수_없다() {
            // given
            Long birthdayCafeId = 3L;
            LoginMember loginMember = new LoginMember(1L);

            // when then
            assertThatThrownBy(() -> birthdayCafeLikeService.like(birthdayCafeId, loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_BIRTHDAY_CAFE_LIKE);
        }

        @Test
        void 존재하지_않는_생일_카페는_할_수_없다() {
            // given
            Long birthdayCafeId = 100L;
            LoginMember loginMember = new LoginMember(1L);

            // when then
            assertThatThrownBy(() -> birthdayCafeLikeService.like(birthdayCafeId, loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(BirthdayCafeErrorCode.NOT_FOUND);
        }

        @Test
        void 취소한다() {
            // given
            Long birthdayCafeId = 1L;
            LoginMember loginMember = new LoginMember(1L);
            birthdayCafeLikeService.like(birthdayCafeId, loginMember);

            // when then
            assertDoesNotThrow(() -> birthdayCafeLikeService.cancelLike(birthdayCafeId, loginMember));
        }

        @Test
        void 찜하지_않은_생일_카페는_취소할_수_없다() {
            // given
            Long birthdayCafeId = 1L;
            LoginMember loginMember = new LoginMember(1L);

            // when then
            assertThatThrownBy(() -> birthdayCafeLikeService.cancelLike(birthdayCafeId, loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_CANCEL);
        }
    }
}
