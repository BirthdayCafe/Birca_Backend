package com.birca.bircabackend.query.dto;

public record CafeSearchResponse(
        Long cafeId,
        String cafeImageUrl,
        String twitterAccount,
        String address
) {
}
