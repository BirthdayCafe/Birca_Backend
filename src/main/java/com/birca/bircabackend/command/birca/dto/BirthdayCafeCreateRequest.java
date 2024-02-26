package com.birca.bircabackend.command.birca.dto;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.Schedule;
import com.birca.bircabackend.command.birca.domain.Visitants;

import java.time.LocalDateTime;

public record BirthdayCafeCreateRequest(
        Long artistId,
        Long cafeId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer minimumVisitant,
        Integer maximumVisitant,
        String twitterAccount,
        String hostPhoneNumber
) {

    public BirthdayCafe toEntity(Long hostId) {
        return BirthdayCafe.create(
                hostId,
                artistId,
                Schedule.of(startDate, endDate),
                Visitants.of(minimumVisitant, maximumVisitant),
                twitterAccount
        );
    }
}
