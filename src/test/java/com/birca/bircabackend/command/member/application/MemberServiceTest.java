package com.birca.bircabackend.command.member.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.MemberFixtureRepository;
import com.birca.bircabackend.command.member.domain.IdentityKey;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.command.member.domain.MemberRole;
import com.birca.bircabackend.command.member.dto.NicknameRegisterRequest;
import com.birca.bircabackend.command.member.dto.RoleChangeRequest;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static com.birca.bircabackend.command.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

@Sql("/fixture/member-fixture.sql")
class MemberServiceTest extends ServiceTest {

    private static final long LOGIN_ID = 1L;

    @Autowired
    private MemberService memberService;

    @Autowired
    private EntityUtil entityUtil;

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

        private final IdentityKey identityKey = IdentityKey.of("1234", "kakao");
        private final String email = "ldk@naver.com";
        private final Member joinMember = Member.join(email, identityKey);

        @Test
        void 한다() {
            // when
            memberService.join(joinMember);

            // then
            assertThat(memberFixtureRepository.findById(joinMember.getId()))
                    .isPresent();
        }

        @Test
        void 이미_가입된_소셜_계정으로는_하지_못_한다() {
            // given
            memberService.join(joinMember);
            Member newJoinMember = Member.join("diffent@email", identityKey);

            // when then
            assertThatThrownBy(() -> memberService.join(newJoinMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.DUPLICATED_IDENTITY_KEY);
        }
    }

    @Nested
    @DisplayName("회원 탈퇴 시")
    class WithdrawMemberTest {

        @Test
        void 정상적으로_탈퇴한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);

            // when
            memberService.withdrawMember(loginMember);
            Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);

            // then
            assertAll(
                    () -> assertThat(member.getRole()).isEqualTo(MemberRole.DELETED),
                    () -> assertThat(member.getIdentityKey().getSocialId()).isNotEqualTo("231323"),
                    () -> assertThat(member.getIdentityKey().getSocialProvider()).isEqualTo("withdraw")
            );
        }

        @Test
        void 존재하지_않는_회원은_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(100L);

            // when then
            assertThatThrownBy(() -> memberService.withdrawMember(loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MEMBER_NOT_FOUND);
        }
    }
}
