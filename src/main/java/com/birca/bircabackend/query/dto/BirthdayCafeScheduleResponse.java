package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record BirthdayCafeScheduleResponse(
        Long birthdayCafeId,
        String nickname,
        ArtistResponse artist,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public static BirthdayCafeScheduleResponse from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        Artist artist = birthdayCafeView.artist();
        Member member = birthdayCafeView.member();
        return new BirthdayCafeScheduleResponse(
                birthdayCafe.getId(),
                member == null ? null : member.getNickname().getValue(),
                new BirthdayCafeScheduleResponse.ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate()
        );
    }

    public static BirthdayCafeScheduleResponse createEmpty() {
        return null;
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}
