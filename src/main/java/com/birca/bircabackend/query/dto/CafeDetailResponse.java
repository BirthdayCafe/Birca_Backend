package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;

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

    public static CafeDetailResponse of(Cafe cafe, List<String> cafeImages) {
        List<CafeMenu> cafeMenus = cafe.getCafeMenus();
        List<CafeOption> cafeOptions = cafe.getCafeOptions();
        return new CafeDetailResponse(
                cafe.getName(),
                cafe.getAddress(),
                cafe.getTwitterAccount(),
                cafe.getBusinessHours(),
                cafeMenus.stream()
                        .map(CafeMenuResponse::from)
                        .toList(),
                cafeOptions.stream()
                        .map(CafeOptionResponse::from)
                        .toList(),
                cafeImages
        );
    }

    public record CafeMenuResponse(
            String name,
            Integer price
    ) {

        public static CafeMenuResponse from(CafeMenu cafeMenu) {
            return new CafeMenuResponse(cafeMenu.getName(), cafeMenu.getPrice());
        }
    }

    public record CafeOptionResponse(
            String name,
            Integer price
    ) {

        public static CafeOptionResponse from(CafeOption cafeOption) {
            return new CafeOptionResponse(cafeOption.getName(), cafeOption.getPrice());
        }
    }
}
