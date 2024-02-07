package com.birca.bircabackend.command.birca.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BirthdayCafeErrorCode implements ErrorCode {

    INVALID_SCHEDULE(5001, 400, "생일카페 시작일은 종료일보다 앞일 수 없습니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
