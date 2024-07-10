package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.Memo;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.time.LocalDateTime;

public record BirthdayCafeScheduleDetailResponseV2(
        Long birthdayCafeId,
        String nickname,
        ArtistResponse artist,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String memo
) {

    public static BirthdayCafeScheduleDetailResponseV2 from(BirthdayCafeView birthdayCafeView) {
        BirthdayCafe birthdayCafe = birthdayCafeView.birthdayCafe();
        ArtistGroup artistGroup = birthdayCafeView.artistGroup();
        Artist artist = birthdayCafeView.artist();
        Member member = birthdayCafeView.member();
        Memo memo = birthdayCafeView.memo();
        return new BirthdayCafeScheduleDetailResponseV2(
                birthdayCafe.getId(),
                member == null ? "" : member.getNickname().getValue(),
                new ArtistResponse(
                        artistGroup == null ? null : artistGroup.getName(),
                        artist.getName()
                ),
                birthdayCafe.getSchedule().getStartDate(),
                birthdayCafe.getSchedule().getEndDate(),
                memo == null ? "" : memo.getContent()
        );
    }

    public static BirthdayCafeScheduleDetailResponseV2 createEmpty() {
        return null;
    }

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}
