package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record MyBirthdayCafeResponse(
        Long birthdayCafeId,
        String mainImageUrl,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String birthdayCafeName,
        String progressState,
        ArtistResponse artist
) {

    public static MyBirthdayCafeResponse from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        BirthdayCafeImage mainImage = birthdayCafeView.mainImage();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        Artist artist = birthdayCafeView.artist();
        return new MyBirthdayCafeResponse(
                birthdayCafe.getId(),
                mainImage == null ? null : mainImage.getImageUrl(),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate(),
                birthdayCafe.getName(),
                birthdayCafe.getProgressState().name(),
                new ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                )

        );
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}