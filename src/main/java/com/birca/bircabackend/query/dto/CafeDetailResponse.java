package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CafeDetailResponse(
        Boolean liked,
        String name,
        String twitterAccount,
        String address,
        String businessHours,
        List<String> cafeImages,
        List<CafeMenuResponse> cafeMenus,
        List<CafeOptionResponse> cafeOptions
) {

    public static CafeDetailResponse from(Boolean liked, Cafe cafe, List<String> cafeImages) {
        return new CafeDetailResponse(
                liked,
                cafe.getName(),
                cafe.getTwitterAccount(),
                cafe.getAddress(),
                cafe.getBusinessHours(),
                cafeImages,
                cafe.getCafeMenus().stream()
                        .map(cm -> new CafeMenuResponse(cm.getName(), cm.getPrice()))
                        .toList(),
                cafe.getCafeOptions().stream()
                        .map(co -> new CafeOptionResponse(co.getName(), co.getPrice()))
                        .toList()
        );
    }

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
