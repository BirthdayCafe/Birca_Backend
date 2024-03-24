package com.birca.bircabackend.query.dto;

import java.time.LocalDateTime;

public record BirthdayCafeResponse(
        Long birthdayCafeId,
        String mainImageUrl,
        LocalDateTime startDate,
        LocalDateTime endDate,
        String birthdayCafeName,
        Boolean isLiked,
        ArtistResponse artist
) {

    public record ArtistResponse(
            String groupName,
            String name
    ) {
    }
}
