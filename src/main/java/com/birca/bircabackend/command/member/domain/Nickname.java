package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Nickname {

    private static final int MAX_LENGTH = 10;

    @Column(name = "nickname", unique = true)
    private String value;

    public Nickname(String value) {
        validateInvalidNickname(value);
        this.value = value;
    }

    private void validateInvalidNickname(String value) {
        if (value.length() > MAX_LENGTH || value.isBlank()) {
            throw BusinessException.from(MemberErrorCode.INVALID_NICKNAME);
        }
    }
}
