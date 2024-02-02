package com.birca.bircabackend.command.artist.exception;

import com.birca.bircabackend.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public enum ArtistErrorCode implements ErrorCode {

    NOT_EXIST_ARTIST(4002, 404, "존재하지 않는 아티스트입니다.")

    ;

    private final int value;
    private final int httpStatusCode;
    private final String message;
}
