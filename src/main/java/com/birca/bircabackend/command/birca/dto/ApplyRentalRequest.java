package com.birca.bircabackend.command.birca.dto;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.PhoneNumber;
import com.birca.bircabackend.command.birca.domain.value.Schedule;
import com.birca.bircabackend.command.birca.domain.value.Visitants;

import java.time.LocalDateTime;

public record ApplyRentalRequest(
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
        return BirthdayCafe.applyRental(
                hostId,
                artistId,
                cafeId,
                Schedule.of(startDate, endDate),
                Visitants.of(minimumVisitant, maximumVisitant),
                twitterAccount,
                PhoneNumber.from(hostPhoneNumber)
        );
    }
}
