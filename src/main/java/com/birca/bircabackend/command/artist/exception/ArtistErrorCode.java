package com.birca.bircabackend.command.artist.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum ArtistErrorCode implements ErrorCode {

    EXCEED_INTEREST_LIMIT(4001, 400, "관심 아티스트는 10명까지 등록할 수 있습니다."),
    NOT_EXIST_ARTIST(4002, 404, "존재하지 않는 아티스트입니다."),
    DUPLICATE_INTEREST_ARTIST(4003, 400, "관심 아티스트는 중복 아티스트로 등록할 수 없습니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
