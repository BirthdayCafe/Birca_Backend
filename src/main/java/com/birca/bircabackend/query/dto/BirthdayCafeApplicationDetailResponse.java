package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record BirthdayCafeApplicationDetailResponse(
        Long birthdayCafeId,
        ArtistResponse artist,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer minimumVisitant,
        Integer maximumVisitant,
        String twitterAccount,
        String hostPhoneNumber
) {

    public static BirthdayCafeApplicationDetailResponse from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        Artist artist = birthdayCafeView.artist();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        return new BirthdayCafeApplicationDetailResponse(
                birthdayCafe.getId(),
                new ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate(),
                birthdayCafe.getVisitants().getMinimumVisitant(),
                birthdayCafe.getVisitants().getMaximumVisitant(),
                birthdayCafe.getTwitterAccount(),
                birthdayCafe.getHostPhoneNumber().getValue()
        );
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}
