package com.birca.bircabackend.command.member.domain;

import com.birca.bircabackend.command.member.exception.MemberErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;

import java.util.Arrays;

public enum MemberRole {

    HOST,
    VISITANT,
    OWNER,
    NOTHING,
    DELETED

    ;

    public static MemberRole from(String name) {
        return Arrays.stream(values())
                .filter(role -> role.name().equals(name.toUpperCase()))
                .findFirst()
                .orElseThrow(() -> BusinessException.from(MemberErrorCode.INVALID_ROLE));
    }
}
