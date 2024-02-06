package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.MemberFixtureRepository;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.command.member.dto.NicknameRegisterRequest;
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
            assertThat(memberFixtureRepository.findById(LOGIN_ID))
                    .map(member -> member.getRole().name())
                    .get()
                    .isEqualTo(role);
        }

        @ParameterizedTest
        @ValueSource(strings = {"HOSTS", "VISITANTS", "INVALID"})
        void 없는_역할로_변경하는_경우를_검증한다(String role) {
            // given
            RoleChangeRequest request = new RoleChangeRequest(role);

            // when then
            assertThatThrownBy(() -> memberService.changeMemberRole(request, loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.INVALID_ROLE);
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

    @Nested
    @DisplayName("닉네임을")
    class RegisterNicknameTest {

        @Test
        void 정상_등록한다() {
            // given
            String nickname = "정상 닉네임";
            NicknameRegisterRequest request = new NicknameRegisterRequest(nickname);

            // when
            memberService.registerNickname(request, loginMember);

            // then
            assertThat(memberFixtureRepository.findById(LOGIN_ID))
                    .map(member -> member.getNickname().getValue())
                    .get()
                    .isEqualTo(nickname);
        }

        @Test
        void 중복_등록하는_경우를_검증한다() {
            // given
            String nickname = "더즈";
            NicknameRegisterRequest request = new NicknameRegisterRequest(nickname);

            // when // then
            assertThatThrownBy(() -> memberService.registerNickname(request, loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.DUPLICATED_NICKNAME);
        }
    }

    @Nested
    @DisplayName("회원 가입을")
    class JoinTest {

        private final String email = "ldk@naver.com";
        private final Member joinMember = Member.join(email, "kakao");

        @Test
        void 한다() {
            // when
            memberService.join(joinMember);

            // then
            assertThat(memberFixtureRepository.findById(joinMember.getId()))
                    .isPresent();
        }

        @Test
        void 이미_가입된_이메일로_하지_못한다() {
            // given
            memberService.join(joinMember);
            Member newJoinMember = Member.join(email, "apple");

            // when then
            assertThatThrownBy(() -> memberService.join(newJoinMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.DUPLICATED_EMAIL);
        }
    }
}
