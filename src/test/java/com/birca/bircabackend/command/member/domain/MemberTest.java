package com.birca.bircabackend.command.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    private final Member member = new Member();

    @Nested
    @DisplayName("회원 역할을")
    class RoleChangeTest {

        @ParameterizedTest
        @ValueSource(strings = {"HOST", "VISITANT", "OWNER"})
        void 존재_하는_역할로_변경_한다(String requestRole) {
            // given
            MemberRole role = MemberRole.valueOf(requestRole);

            // when
            member.changeRole(role);

            // then
            assertThat(member.getRole()).isEqualTo(role);
        }
    }
}
