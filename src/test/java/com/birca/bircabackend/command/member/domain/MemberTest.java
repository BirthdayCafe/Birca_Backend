package com.birca.bircabackend.command.member.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class MemberTest {

    @Nested
    @DisplayName("회원 역할을")
    class RoleChangeTest {

        private final Member member = new Member();

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

    @Nested
    @DisplayName("회원을 처음 생성하면")
    class JoinTest {

        @Test
        void 방문자로_생성된다() {
            // given
            String email = "ldk@gmail.com";
            IdentityKey identityKey = IdentityKey.of("2342", "kakao");

            // when
            Member actual = Member.join(email, identityKey);

            // then
            assertThat(actual.getRole()).isEqualTo(MemberRole.NOTHING);
        }
    }
}
