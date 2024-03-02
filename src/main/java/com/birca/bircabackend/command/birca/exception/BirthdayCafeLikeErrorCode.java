package com.birca.bircabackend.command.birca.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BirthdayCafeLikeErrorCode implements ErrorCode {

    INVALID_LIKE_REQUEST(6001, 400, "대관 대기, 취소 상태에서는 찜하기를할 수 없습니다."),
    ALREADY_LIKED(6002, 400, "이미 찜한 생일 카페는 중복으로 찜할 수 없습니다."),
    NOT_FOUND(6003, 404, "존재하지 않는 찜한 생일 카페입니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
