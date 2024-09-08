package com.birca.bircabackend.command.cafe.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum CafeImageErrorCode implements ErrorCode {

    NOT_FOUND(7001, 404, "존재하지 않는 카페 이미지입니다."),
    INVALID_UPLOAD_SIZE_REQUEST(7002, 400, "카페 이미지는 5장까지 저장할 수 있습니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
