package com.birca.bircabackend.command.like.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum LikeErrorCode implements ErrorCode {

    INVALID_BIRTHDAY_CAFE_LIKE(6001, 400, "대관 대기, 취소 상태에서는 생일카페에 찜하기를 할 수 없습니다."),
    INVALID_CANCEL(6002, 400, "찜하지 않은 상태에서 찜을 취소할 수 없습니다."),
    INVALID_CAFE_LIKE(6003, 400, "사업자등록증이 승인되지 않은 카페는 찜하기를 할 수 없습니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
