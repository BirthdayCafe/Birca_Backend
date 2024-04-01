package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.birca.domain.value.CongestionState;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.domain.value.SpecialGoodsStockState;
import com.birca.bircabackend.command.birca.domain.value.Visibility;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;
import java.util.List;

public record BirthdayCafeDetailResponse(
        CafeResponse cafe,
        ArtistResponse artist,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String birthdayCafeName,
        Integer likeCount,
        Boolean isLiked,
        String twitterAccount,
        Integer minimumVisitant,
        Integer maximumVisitant,
        CongestionState congestionState,
        Visibility visibility,
        ProgressState progressState,
        SpecialGoodsStockState specialGoodsStockState,
        String mainImage,
        List<String> defaultImages
) {

    public static BirthdayCafeDetailResponse of(BirthdayCafeView birthdayCafeView,
                                                Integer likeCount,
                                                List<String> defaultImages,
                                                List<String> cafeImages
    ) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        Artist artist = birthdayCafeView.artist();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        BirthdayCafeImage mainImage = birthdayCafeView.mainImage();
        Cafe cafe = birthdayCafeView.cafe();
        Like like = birthdayCafeView.like();
        return new BirthdayCafeDetailResponse(
                new CafeResponse(
                        cafe.getName(),
                        cafe.getAddress(),
                        cafeImages
                ),
                new ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate(),
                birthdayCafe.getName(),
                likeCount,
                like != null,
                birthdayCafe.getTwitterAccount(),
                birthdayCafe.getVisitants().getMinimumVisitant(),
                birthdayCafe.getVisitants().getMaximumVisitant(),
                birthdayCafe.getCongestionState(),
                birthdayCafe.getVisibility(),
                birthdayCafe.getProgressState(),
                birthdayCafe.getSpecialGoodsStockState(),
                mainImage == null ? null : mainImage.getImageUrl(),
                defaultImages
        );
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }

    public record CafeResponse(
            String name,
            String address,
            List<String> images
    ) {
    }
}
