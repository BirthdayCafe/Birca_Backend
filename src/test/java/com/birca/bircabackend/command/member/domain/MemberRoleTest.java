package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MemberRoleTest {

    @Nested
    class CreateTest {

        @ParameterizedTest
        @ValueSource(strings = {"HOST", "VISITANT", "OWNER"})
        void 올바른_이름으로_역할을_생성한다(String name) {
            // when
            MemberRole role = MemberRole.from(name);

            // then
            assertThat(role).isEqualTo(MemberRole.valueOf(name));
        }

        @ParameterizedTest
        @ValueSource(strings = {"HOSTS", "VISITANTS", "INVALID"})
        void 없는_이름으로_역할을_생성하는_경우를_검증한다(String name) {
            // when then
            assertThatThrownBy(() -> MemberRole.from(name))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.INVALID_ROLE);
        }
    }
}
