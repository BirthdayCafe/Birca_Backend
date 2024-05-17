package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record BirthdayCafeApplicationResponse(
        Long birthdayCafeId,
        String hostName,
        ArtistResponse artist,
        LocalDateTime startDate,
        LocalDateTime endDate

) {

    public static BirthdayCafeApplicationResponse from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        Artist artist = birthdayCafeView.artist();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        Member member = birthdayCafeView.member();
        return new BirthdayCafeApplicationResponse(
                birthdayCafe.getId(),
                member.getNickname().getValue(),
                new BirthdayCafeApplicationResponse.ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate()
        );
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}
