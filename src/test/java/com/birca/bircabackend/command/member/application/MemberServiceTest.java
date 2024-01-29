package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.member.MemberFixtureRepository;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

        @Test
        void 변경할_때_없는_회원인_경우를_검증한다() {
            // given
            RoleChangeRequest request = new RoleChangeRequest("HOST");

            // when // then
            assertThatThrownBy(() -> memberService.changeMemberRole(request, new LoginMember(100L)))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }
}