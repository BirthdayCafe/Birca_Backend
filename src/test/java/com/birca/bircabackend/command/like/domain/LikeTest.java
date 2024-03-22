package com.birca.bircabackend.command.like.domain;

import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.*;

class LikeTest {

    @Nested
    @DisplayName("Like는 validator가")
    class CreateTest {

        private static final Long MEMBER_ID = 1L;
        private static final LikeTarget TARGET = new LikeTarget(1L, LikeTargetType.BIRTHDAY_CAFE);
        private LikeValidator likeValidator;

        @BeforeEach
        void mockValidator() {
            likeValidator = mock(LikeValidator.class);
        }

        @Test
        void 예외를_발생시키지_않으면_생성한다() {
            // when
            Like like = Like.create(MEMBER_ID, TARGET, likeValidator);

            // then
            assertAll(
                    () -> assertThat(like.getVisitantId()).isEqualTo(MEMBER_ID),
                    () -> assertThat(like.getTarget()).isEqualTo(TARGET),
                    () -> verify(likeValidator).validate(like)
            );


        }

        @Test
        void 예외를_발생시키면_생성하지_못한다() {
            // given
            doThrow(BusinessException.class)
                    .when(likeValidator).validate(any());

            // when then
            assertAll(
                    () -> assertThatThrownBy(() -> Like.create(MEMBER_ID, TARGET, likeValidator))
                            .isInstanceOf(BusinessException.class),
                    () -> verify(likeValidator).validate(any())
            );
        }
    }
}
