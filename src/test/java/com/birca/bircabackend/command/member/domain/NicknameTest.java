package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class NicknameTest {

    @Nested
    @DisplayName("닉네임 생성 시")
    class CreateTest {

        @ParameterizedTest
        @ValueSource(strings = {"nickname", "닉네임", "일", "0123456789"})
        void 정상적으로_생성한다(String value) {
            // when
            Nickname nickname = new Nickname(value);

            // then
            assertThat(nickname.getValue()).isEqualTo(value);
        }

        @ParameterizedTest
        @ValueSource(strings = {"", " ", "12345678910", "abcdefghijklmn"})
        void 빈_값이거나_10자를_넘는_경우를_검증한다(String value) {
            // when // then
            assertThatThrownBy(() -> new Nickname(value))
                    .isInstanceOf(BusinessException.class)
                    .extracting("errorCode")
                    .isEqualTo(MemberErrorCode.INVALID_NICKNAME);
        }
    }
}
