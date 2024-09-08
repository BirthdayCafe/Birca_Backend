package com.birca.bircabackend.command.cafe.dto;

public record CafeUpdateRequest(
        String cafeName,
        String cafeAddress,
        String twitterAccount,
        String businessHours
) {
}
