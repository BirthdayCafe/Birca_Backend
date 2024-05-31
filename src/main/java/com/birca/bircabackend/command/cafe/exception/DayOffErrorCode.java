package com.birca.bircabackend.command.cafe.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum DayOffErrorCode implements ErrorCode {

    DAY_OFF_DATE(9001, 400, "해당 날짜는 카페 휴무일입니다.")
    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
