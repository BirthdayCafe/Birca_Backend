package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.query.repository.model.CafeView;

public record CafeSearchResponse(
        Long cafeId,
        Boolean liked,
        String cafeImageUrl,
        String twitterAccount,
        String address
) {

    public static CafeSearchResponse from(CafeView cafeView) {
        Cafe cafe = cafeView.cafe();
        CafeImage cafeImage = cafeView.cafeImage();
        Like like = cafeView.like();
        return new CafeSearchResponse(
                cafe.getId(),
                like != null,
                cafeImage.getImageUrl(),
                cafe.getTwitterAccount(),
                cafe.getAddress()
        );
    }
}
