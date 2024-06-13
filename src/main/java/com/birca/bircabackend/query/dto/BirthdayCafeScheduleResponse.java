package com.birca.bircabackend.query.dto;

import java.time.LocalDateTime;

public record BirthdayCafeScheduleResponse(
        LocalDateTime startDate,
        LocalDateTime endDate
) {
}
