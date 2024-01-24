package com.birca.bircabackend.common.exception;

public record ExceptionResponse(Integer errorCode, String message) {

    public static ExceptionResponse from(ErrorCode errorCode) {
        return new ExceptionResponse(errorCode.value(), errorCode.message());
    }
}
