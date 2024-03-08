package com.birca.bircabackend.command.cafe.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BusinessLicenseErrorCode implements ErrorCode {

    NOT_REGISTERED_BUSINESS_LICENSE_NUMBER(2001, 400, "국세청에 등록되지 않은 사업자등록번호입니다."),
    BUSINESS_LICENSE_STATUS_ERROR(2002, 400, "사업자등록증 상태 조회 중 에러가 발생했습니다."),
    DUPLICATE_BUSINESS_LICENSE_NUMBER(2003, 400, "이미 등록된 사업자등록증입니다."),
    BUSINESS_LICENSE_NOT_FOUND(2004, 404, "존재하지 않는 사업자등록증입니다."),
    OVER_MAX_OCR_REQUEST_COUNT(2005, 400, "OCR 요청은 5회까지만 가능합니다."),
    INVALID_BUSINESS_LICENSE_NUMBER(2006, 400, "형식에 맞지 않는 사업자등록번호입니다."),
    INVALID_UPLOAD_COUNT(2007, 400, "처음 사업자등록증 업로드 횟수는 0이어야 합니다.")
    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
