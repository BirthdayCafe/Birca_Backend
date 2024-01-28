package com.birca.bircabackend.common.exception.presentation;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.ErrorCode;
import com.birca.bircabackend.common.exception.ExceptionResponse;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionController {

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleBusinessException(BusinessException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ExceptionResponse.from(errorCode));
    }

    @ExceptionHandler
    public ResponseEntity<ExceptionResponse> handleUnExpectedException(Exception exception) {
        InternalServerErrorCode errorCode = new InternalServerErrorCode(exception.getMessage());
        return ResponseEntity.status(errorCode.getHttpStatusCode())
                .body(ExceptionResponse.from(errorCode));
    }
}
