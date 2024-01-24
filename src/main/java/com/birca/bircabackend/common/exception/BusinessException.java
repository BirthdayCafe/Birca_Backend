package com.birca.bircabackend.common.exception;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    private BusinessException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
    }

    public static BusinessException from(ErrorCode errorCode) {
        return new BusinessException(errorCode);
    }
}
