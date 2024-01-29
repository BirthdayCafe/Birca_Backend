package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.member.MemberFixtureRepository;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/fixture/member-fixture.sql")
class MemberServiceTest extends ServiceTest {

    private static final long LOGIN_ID = 1L;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberFixtureRepository memberFixtureRepository;

    private final LoginMember loginMember = new LoginMember(LOGIN_ID);

    @Nested
    @DisplayName("회원 역할을")
    class RoleChangeTest {

        @ParameterizedTest
        @ValueSource(strings = {"HOST", "VISITANT", "OWNER"})
        void 존재_하는_역할로_변경_한다(String role) {
            // given
            RoleChangeRequest request = new RoleChangeRequest(role);

            // when
            memberService.changeMemberRole(request, loginMember);

            // then
            assertThat(memberFixtureRepository.findById(1L))
                    .map(member -> member.getRole().name())
                    .get()
                    .isEqualTo(role);
        }
    }
}