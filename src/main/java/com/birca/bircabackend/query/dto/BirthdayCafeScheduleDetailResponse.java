package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record BirthdayCafeScheduleDetailResponse(
        Long birthdayCafeId,
        String nickname,
        ArtistResponse artist,
        LocalDateTime startDate,
        LocalDateTime endDate
) {

    public static BirthdayCafeScheduleDetailResponse from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        Artist artist = birthdayCafeView.artist();
        Member member = birthdayCafeView.member();
        return new BirthdayCafeScheduleDetailResponse(
                birthdayCafe.getId(),
                member == null ? null : member.getNickname().getValue(),
                new BirthdayCafeScheduleDetailResponse.ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate()
        );
    }

    public static BirthdayCafeScheduleDetailResponse createEmpty() {
        return null;
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}
