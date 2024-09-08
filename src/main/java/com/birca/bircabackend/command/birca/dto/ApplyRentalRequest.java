package com.birca.bircabackend.command.birca.dto;

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
}
