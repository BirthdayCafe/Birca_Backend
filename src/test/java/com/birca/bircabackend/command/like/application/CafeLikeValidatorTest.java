package com.birca.bircabackend.command.like.application;

import com.birca.bircabackend.command.cafe.domain.BusinessLicenseRepository;
import com.birca.bircabackend.command.cafe.domain.Cafe;
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
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.jdbc.Sql;

import static com.birca.bircabackend.command.cafe.exception.CafeErrorCode.NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@Sql("/fixture/cafe-fixture.sql")
class CafeLikeValidatorTest {

    private static final FixtureMonkey fixtureMonkey = FixtureMonkey.builder()
            .objectIntrospector(FieldReflectionArbitraryIntrospector.INSTANCE)
            .build();

    @InjectMocks
    private CafeLikeValidator likeValidator;

    @Mock
    private EntityUtil entityUtil;

    @Mock
    private BusinessLicenseRepository businessLicenseRepository;

    @Nested
    @DisplayName("카페 찜 가능 여부를 검증할 때")
    class ValidateTest {

        private static final Long TARGET_ID = 1L;
        private static final LikeTarget LIKE_TARGET = new LikeTarget(TARGET_ID, LikeTargetType.CAFE);
        private static final Like LIKE = fixtureMonkey.giveMeBuilder(Like.class)
                .set("target", LIKE_TARGET)
                .sample();
        private static final Cafe CAFE = fixtureMonkey.giveMeBuilder(Cafe.class)
                .set("ownerId", 1L)
                .set("businessLicenseId", 1L)
                .sample();

        @Test
        void 사업자등록증이_승인된_카페는_예외가_발생하지_않는다() {
            // given
            given(entityUtil.getEntity(Cafe.class, TARGET_ID, NOT_FOUND))
                    .willReturn(CAFE);
            given(businessLicenseRepository.existsByRegistrationApprovedAndOwnerId(1L))
                    .willReturn(true);

            // when then
            assertDoesNotThrow(() -> likeValidator.validate(LIKE));
        }

        @Test
        void 사업자등록증이_승인되지_않은_카페는_예외가_발생한다() {
            // given
            given(entityUtil.getEntity(Cafe.class, TARGET_ID, NOT_FOUND))
                    .willReturn(CAFE);
            given(businessLicenseRepository.existsByRegistrationApprovedAndOwnerId(1L))
                    .willReturn(false);

            // when then
            assertThatThrownBy(() -> likeValidator.validate(LIKE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(LikeErrorCode.INVALID_CAFE_LIKE);
        }
    }
}
