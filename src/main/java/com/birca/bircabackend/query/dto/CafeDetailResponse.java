package com.birca.bircabackend.query.dto;

import java.util.List;

public record CafeDetailResponse(
        String cafeName,
        String cafeAddress,
        String twitterAccount,
        String businessHours,
        List<CafeMenuResponse> cafeMenus,
        List<CafeOptionResponse> cafeOptions,
        List<String> cafeImages
) {

    public record CafeMenuResponse(
            String name,
            Integer price
    ) {
    }

    public record CafeOptionResponse(
            String name,
            Integer price
    ) {
    }
}
