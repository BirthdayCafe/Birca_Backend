package com.birca.bircabackend.command.cafe.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum CafeErrorCode implements ErrorCode {

    NOT_FOUND(8001, 404, "존재하지 않는 카페입니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
