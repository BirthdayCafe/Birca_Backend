package com.birca.bircabackend.command.birca.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum BirthdayCafeErrorCode implements ErrorCode {

    INVALID_SCHEDULE(5001, 400, "생일카페 시작일은 종료일 보다 앞일 수 없습니다."),
    INVALID_VISITANTS(5002, 400, "최소 방문자는 최대 방문자 보다 클 수 없고 자연수여야 합니다."),
    RENTAL_PENDING_EXISTS(5003, 400, "대관 대기 상태인 생일 카페가 이미 존재합니다."),
    INVALID_PHONE_NUMBER(5004, 400, "올바르지 않은 연락처 형식입니다."),
    INVALID_CANCEL_RENTAL(5005, 400, "대관 대기 상태에서만 취소할 수 있습니다."),
    NOT_FOUND(5006, 404, "존재하지 않는 생일 카페입니다."),
    UNAUTHORIZED_CANCEL(5007, 400, "대관 취소 권한이 없는 회원입니다."),
    INVALID_UPDATE(5008, 400, "현재 상태로는 해당 정보를 변경할 수 없습니다."),
    UNAUTHORIZED_UPDATE(5009, 400, "생일 카페 변경 권한이 없는 회원입니다."),
    INVALID_UPLOAD_SIZE_REQUEST(5010, 400, "생일 카페 사진은 최대 10장까지 업로드할 수 있습니다."),
    RENTAL_ALREADY_EXISTS(5011, 400, "이미 대관되거나 진행 중인 생일 카페가 존재합니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
