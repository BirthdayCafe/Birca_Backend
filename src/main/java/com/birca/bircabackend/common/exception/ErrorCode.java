package com.birca.bircabackend.common.exception;

public interface ErrorCode {

    int getValue();

    int getHttpStatusCode();

    String getMessage();
}
