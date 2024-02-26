package com.birca.bircabackend.command.birca.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BirthdayCafeErrorCode implements ErrorCode {

    INVALID_SCHEDULE(5001, 400, "생일카페 시작일은 종료일 보다 앞일 수 없습니다."),
    INVALID_VISITANTS(5002, 400, "최소 방문자는 최대 방문자 보다 클 수 없고 자연수여야 합니다."),
    RENTAL_PENDING_EXISTS(5003, 400, "대관 대기 상태인 생일 카페가 이미 존재합니다."),
    INVALID_PHONE_NUMBER(5004, 400, "올바르지 않은 연락처 형식입니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
