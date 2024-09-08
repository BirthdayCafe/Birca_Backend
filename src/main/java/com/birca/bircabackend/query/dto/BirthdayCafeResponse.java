package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record BirthdayCafeResponse(
        Long birthdayCafeId,
        String mainImageUrl,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String birthdayCafeName,
        Boolean isLiked,
        ArtistResponse artist,
        CafeResponse cafe
) {


    public static BirthdayCafeResponse from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        BirthdayCafeImage mainImage = birthdayCafeView.mainImage();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        Artist artist = birthdayCafeView.artist();
        Like like = birthdayCafeView.like();
        Cafe cafe = birthdayCafeView.cafe();
        return new BirthdayCafeResponse(
                birthdayCafe.getId(),
                mainImage == null ? null : mainImage.getImageUrl(),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate(),
                birthdayCafe.getName(),
                like != null,
                new ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                new CafeResponse(cafe.getAddress())
        );
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }

    public record CafeResponse(
            String address
    ) {
    }
}
