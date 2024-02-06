package com.birca.bircabackend.command.cafe.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BusinessLicenseErrorCode implements ErrorCode {

    NOT_REGISTERED_BUSINESS_LICENSE_NUMBER(2001, 400, "국세청에 등록되지 않은 사업자등록번호입니다."),
    INVALID_TAX_OFFICE_CODE_LENGTH(2002, 400, "세무서 번호는 3자리여야 합니다."),
    INVALID_BUSINESS_TYPE_CODE_LENGTH(2003, 400, "사업자 구분 번호는 2자리여야 합니다."),
    INVALID_SERIAL_CODE_LENGTH(2004, 400, "일련번호는 5자리여야 합니다."),
    BUSINESS_LICENSE_STATUS_ERROR(2005, 400, "사업자등록증 상태 조회 중 에러가 발생했습니다."),
    DUPLICATE_BUSINESS_LICENSE_NUMBER(2006, 400, "이미 등록된 사업자등록증입니다."),
    BUSINESS_LICENSE_NOT_FOUND(2007, 404, "존재하지 않는 사업자등록증입니다."),
    OVER_MAX_OCR_REQUEST_COUNT(2008, 400, "OCR 요청은 3회까지만 가능합니다."),
    INVALID_BUSINESS_LICENSE_NUMBER_FORM(2009, 400, "형식에 맞지 않는 사업자등록번호입니다.")
    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
