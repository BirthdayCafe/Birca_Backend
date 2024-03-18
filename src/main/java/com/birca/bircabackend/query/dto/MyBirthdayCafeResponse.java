package com.birca.bircabackend.query.dto;

import java.time.LocalDateTime;

public record MyBirthdayCafeResponse(
        BirthdayCafe birthdayCafe
) {

    public record BirthdayCafe(
            Long id,
            String mainImageUrl,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String name,
            String progressState,
            Artist artist

    ) {
    }

    public record Artist(
            String groupName,
            String name
    ){
    }
}
