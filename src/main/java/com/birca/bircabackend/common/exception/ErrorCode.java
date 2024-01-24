package com.birca.bircabackend.common.exception;

public interface ErrorCode {

    int value();

    int httpStatusCode();

    String message();
}
