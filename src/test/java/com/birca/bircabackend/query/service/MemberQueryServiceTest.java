package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.NicknameCheckResponse;
import com.birca.bircabackend.query.dto.ProfileResponse;
import com.birca.bircabackend.query.dto.RoleResponse;
import com.birca.bircabackend.support.enviroment.ServiceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Sql("/fixture/member-fixture.sql")
class MemberQueryServiceTest extends ServiceTest {

    @Autowired
    private MemberQueryService memberQueryService;

    @ParameterizedTest
    @CsvSource({"새 닉네임, false", "더즈, true", "더즈1, false"})
    void 중복된_닉네임인지_검사한다(String requestNickname, boolean isDuplicated) {
        // when
        NicknameCheckResponse response = memberQueryService.checkNickname(requestNickname);

        // then
        assertThat(response.success()).isEqualTo(isDuplicated);
    }

    @Nested
    @DisplayName("프로필을 조회할 때")
    class GetNyNicknameTest {

        @Test
        void 정상적으로_조회한다() {
            // given
            LoginMember loginMember = new LoginMember(1L);

            // when
            ProfileResponse response = memberQueryService.getMyProfile(loginMember);

            // then
            assertThat(response.nickname()).isEqualTo("더즈");
        }

        @Test
        void 존재하지_않는_회원은_예외가_발생한다() {
            // given
            LoginMember loginMember = new LoginMember(100L);

            // when then
            assertThatThrownBy(() -> memberQueryService.getMyProfile(loginMember))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.MEMBER_NOT_FOUND);
        }
    }

    @ParameterizedTest
    @CsvSource({"1, VISITANT", "100, NOTHING"})
    void 회원_역할을_조회한다(Long loginMemberId, String expectedRole) {
        // given
        LoginMember loginMember = new LoginMember(loginMemberId);

        // when
        RoleResponse actual = memberQueryService.getMyRole(loginMember);

        // then
        assertThat(actual.role()).isEqualTo(expectedRole);
    }
}
