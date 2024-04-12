package com.birca.bircabackend.command.cafe.dto;

import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.query.dto.CafeDetailResponse;

import java.util.List;

public record CafeUpdateRequest(
        String cafeName,
        String cafeAddress,
        String twitterAccount,
        String businessHours,
        List<CafeMenuResponse> cafeMenus,
        List<CafeOptionResponse> cafeOptions
) {

    public record CafeMenuResponse(
            String name,
            Integer price
    ) {

        public static CafeDetailResponse.CafeMenuResponse from(CafeMenu cafeMenu) {
            return new CafeDetailResponse.CafeMenuResponse(cafeMenu.getName(), cafeMenu.getPrice());
        }
    }

    public record CafeOptionResponse(
            String name,
            Integer price
    ) {

        public static CafeDetailResponse.CafeOptionResponse from(CafeOption cafeOption) {
            return new CafeDetailResponse.CafeOptionResponse(cafeOption.getName(), cafeOption.getPrice());
        }
    }
}
