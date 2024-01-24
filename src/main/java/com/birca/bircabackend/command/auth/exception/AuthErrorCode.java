package com.birca.bircabackend.command.auth.exception;

import com.birca.bircabackend.common.exception.ErrorCode;

public enum AuthErrorCode implements ErrorCode {

    NOT_EXISTS_TOKEN(1001, 401, "토큰이 존재하지 않습니다.");

    private final int value;
    private final int httpStatusCode;
    private final String message;

    AuthErrorCode(int value, int httpStatusCode, String message) {
        this.value = value;
        this.httpStatusCode = httpStatusCode;
        this.message = message;
    }

    @Override
    public int value() {
        return value;
    }

    @Override
    public int httpStatusCode() {
        return httpStatusCode;
    }

    @Override
    public String message() {
        return message;
    }
}
