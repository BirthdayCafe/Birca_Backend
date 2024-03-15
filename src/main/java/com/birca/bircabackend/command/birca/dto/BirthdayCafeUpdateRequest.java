package com.birca.bircabackend.command.birca.dto;

public record BirthdayCafeUpdateRequest(
        String birthdayCafeName,
        String birthdayCafeTwitterAccount
) {
}
