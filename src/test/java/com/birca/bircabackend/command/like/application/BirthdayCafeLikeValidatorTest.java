package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.command.like.domain.LikeTarget;
import com.birca.bircabackend.command.like.domain.LikeTargetType;
import com.birca.bircabackend.command.like.exception.LikeErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.navercorp.fixturemonkey.FixtureMonkey;
import com.navercorp.fixturemonkey.api.introspector.FieldReflectionArbitraryIntrospector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.INCLUDE;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class BirthdayCafeLikeValidatorTest {

    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .build();

    @InjectMocks
    private BirthdayCafeLikeValidator likeValidator;

    @Mock
    private EntityUtil entityUtil;

    @Nested
    @DisplayName("생일 카페 찜 가능 여부를 검증할 때")
    class ValidateTest {

        private static final Long TARGET_ID = 1L;
        private static final LikeTarget LIKE_TARGET = new LikeTarget(TARGET_ID, LikeTargetType.BIRTHDAY_CAFE);
        private static final Like LIKE = fixtureMonkey.giveMeBuilder(Like.class)
                .set("target", LIKE_TARGET)
                .sample();

        @ParameterizedTest
        @EnumSource(mode = EXCLUDE, names = {"RENTAL_PENDING", "RENTAL_CANCELED"})
        void 대관_대기이거나_대관_취소가_아니면_예외를_발생시키지_않는다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("progressState", progressState)
                    .sample();

            given(entityUtil.getEntity(BirthdayCafe.class, TARGET_ID, NOT_FOUND))
                    .willReturn(birthdayCafe);

            // when then
            assertDoesNotThrow(() -> likeValidator.validate(LIKE));
        }

        @ParameterizedTest
        @EnumSource(mode = INCLUDE, names = {"RENTAL_PENDING", "RENTAL_CANCELED"})
        void 대관_대기이거나_대관_취소일_때는_예외를_발생시킨다(ProgressState progressState) {
            // given
            BirthdayCafe birthdayCafe = fixtureMonkey.giveMeBuilder(BirthdayCafe.class)
                    .set("progressState", progressState)
                    .sample();

            given(entityUtil.getEntity(BirthdayCafe.class, TARGET_ID, NOT_FOUND))
                    .willReturn(birthdayCafe);

            // when then
            assertThatThrownBy(() -> likeValidator.validate(LIKE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_BIRTHDAY_CAFE_LIKE);
        }
    }
}