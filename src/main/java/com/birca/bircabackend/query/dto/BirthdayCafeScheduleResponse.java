package com.birca.bircabackend.query.dto;

import java.time.LocalDateTime;

public record BirthdayCafeScheduleResponse(
        Long birthdayCafeId,
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
