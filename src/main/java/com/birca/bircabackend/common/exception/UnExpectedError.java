package com.birca.bircabackend.common.exception;

public record UnExpectedError(String message) implements ErrorCode {

    @Override
    public int value() {
        return 0;
    }

    @Override
    public int httpStatusCode() {
        return 500;
    }
}
