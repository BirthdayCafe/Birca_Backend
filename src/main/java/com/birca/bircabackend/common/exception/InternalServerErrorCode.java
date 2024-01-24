package com.birca.bircabackend.common.exception;

import lombok.Getter;

@Getter
public class InternalServerErrorCode implements ErrorCode {

    private final int value = 1;
    private final int httpStatusCode = 500;
    private final String message;

    public InternalServerErrorCode(String message) {
        this.message = message;
    }
}
