package com.birca.bircabackend.command.auth.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum AuthErrorCode implements ErrorCode {

    NOT_EXISTS_TOKEN(1001, 401, "토큰이 존재하지 않습니다."),
    INVALID_EXISTS_TOKEN(1002, 401, "유효하지 않은 토큰입니다."),
    NOT_EXISTS_PROVIDER(1003, 400, "해당 이름의 소셜 로그인은 제공하지 않습니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
