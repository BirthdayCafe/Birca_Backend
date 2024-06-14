package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.birca.domain.value.Schedule;

import java.time.LocalDateTime;

public record BirthdayCafeScheduleResponse(
        Integer startYear,
        Integer startMonth,
        Integer startDay,
        Integer endYear,
        Integer endMonth,
        Integer endDay
) {
    public static BirthdayCafeScheduleResponse of(Schedule schedule) {
        LocalDateTime startDate = schedule.getStartDate();
        LocalDateTime endDate = schedule.getEndDate();
        return new BirthdayCafeScheduleResponse(
                startDate.getYear(),
                startDate.getMonthValue(),
                startDate.getDayOfMonth(),
                endDate.getYear(),
                endDate.getMonthValue(),
                endDate.getDayOfMonth()
        );
    }
}

