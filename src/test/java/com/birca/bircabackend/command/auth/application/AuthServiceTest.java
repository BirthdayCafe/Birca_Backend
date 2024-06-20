package com.birca.bircabackend.command.auth.application;

import com.birca.bircabackend.command.auth.application.oauth.OAuthMember;
import com.birca.bircabackend.command.auth.dto.LoginResponse;
import com.birca.bircabackend.command.member.domain.MemberRole;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class AuthServiceTest extends ServiceTest {

    @Autowired
    private AuthService authService;

    @Nested
    @DisplayName("소셜 로그인을")
    class LoginTest {

        private final OAuthMember oAuthMember = new OAuthMember("2435", "ldk@gmail.com", "kakao");

        @Test
        void 처음_가입한_회원이_한다() {
            // when
            LoginResponse response = authService.login(oAuthMember);

            // then
            assertAll(
                    () -> assertThat(response.accessToken()).isNotNull(),
                    assertThat(response.isNewMember())::isTrue,
                    () -> assertThat(response.lastRole()).isEqualTo(MemberRole.NOTHING.name())
            );
        }

        @Test
        void 기존_회원이_한다() {
            // given
            authService.login(oAuthMember);

            // when
            LoginResponse response = authService.login(oAuthMember);

            // then
            assertAll(
                    () -> assertThat(response.accessToken()).isNotNull(),
                    assertThat(response.isNewMember())::isFalse,
                    () -> assertThat(response.lastRole()).isEqualTo(MemberRole.NOTHING.name())
            );
        }
    }
}
