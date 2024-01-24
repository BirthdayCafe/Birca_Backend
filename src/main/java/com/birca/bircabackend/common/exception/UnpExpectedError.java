package com.birca.bircabackend.common.exception;

public class UnpExpectedError implements ErrorCode {

    private final String message;

    public UnpExpectedError(String message) {
        this.message = message;
    }

    @Override
    public int getValue() {
        return 0;
    }

    @Override
    public int getHttpStatusCode() {
        return 500;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
