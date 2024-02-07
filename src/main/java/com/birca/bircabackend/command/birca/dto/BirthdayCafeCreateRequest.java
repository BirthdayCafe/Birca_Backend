package com.birca.bircabackend.command.birca.dto;

import java.time.LocalDateTime;

public record BirthdayCafeCreateRequest(
        Long artistId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Integer minimumVisitant,
        Integer maximumVisitant,
        String twitterAccount
) {
}
