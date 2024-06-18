package com.birca.bircabackend.query.dto;

public record RoleResponse(String role) {

    private static final String ROLE = "NOTHING";

    public static RoleResponse createEmpty() {
        return new RoleResponse(ROLE);
    }
}
