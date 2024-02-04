package com.birca.bircabackend.command.cafe.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BusinessLicenseErrorCode implements ErrorCode {

    NOT_BUSINESS_LICENSE_OR_IMAGE_PROCESSING_FAILED(2001, 400, "사업자등록증이 아니거나 이미지 처리에 실패했습니다."),
    NOT_REGISTERED_BUSINESS_LICENSE_NUMBER(2002, 400, "국세청에 등록되지 않은 사업자등록번호입니다."),
    INVALID_TAX_OFFICE_CODE_LENGTH(2003, 400, "세무서 번호는 3자리여야 합니다."),
    INVALID_BUSINESS_TYPE_CODE_LENGTH(2004, 400, "사업자 구분 번호는 2자리여야 합니다."),
    INVALID_SERIAL_CODE_LENGTH(2005, 400, "일련번호는 5자리여야 합니다."),
    BUSINESS_LICENSE_STATUS_ERROR(2006, 400, "사업자등록증 상태 조회 중 에러가 발생했습니다."),
    DUPLICATE_BUSINESS_LICENSE_NUMBER(2007, 400, "이미 등록된 사업자등록증입니다."),
    BUSINESS_LICENSE_NOT_FOUND(2008, 404, "존재하지 않는 사업자등록증입니다."),
    OVER_MAX_OCR_REQUEST_COUNT(2009, 400, "OCR 요청은 3회까지만 가능합니다.")
    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
