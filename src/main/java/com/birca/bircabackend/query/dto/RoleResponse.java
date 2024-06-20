package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.member.domain.MemberRole;

public record RoleResponse(String role) {

    public static RoleResponse createEmpty() {
        return new RoleResponse(MemberRole.NOTHING.name());
    }
}
