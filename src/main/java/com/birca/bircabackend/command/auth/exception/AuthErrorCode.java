package com.birca.bircabackend.command.auth.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum AuthErrorCode implements ErrorCode {

    NOT_EXISTS_TOKEN(1001, 401, "토큰이 존재하지 않습니다.");

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
