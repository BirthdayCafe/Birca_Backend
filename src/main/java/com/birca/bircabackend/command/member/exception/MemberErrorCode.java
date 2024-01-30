package com.birca.bircabackend.command.member.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum MemberErrorCode implements ErrorCode {

    DUPLICATED_NICKNAME(3001 , 400, "중복된 닉네임입니다."),
    INVALID_NICKNAME(3002, 400, "닉네임은 비어있거나 10자를 넘을 수 없습니다."),
    INVALID_ROLE(3003, 400, "존재하지 않는 역할입니다."),
    MEMBER_NOT_FOUND(3004, 404, "존재하지 않는 회원입니다.")


    ;


    private final int value;
    private final int httpStatusCode;
    private final String message;
}
