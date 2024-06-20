package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.birca.domain.value.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record CafeRentalDateResponse(
        Integer startYear,
        Integer startMonth,
        Integer startDay,
        Integer endYear,
        Integer endMonth,
        Integer endDay
) {

    public static CafeRentalDateResponse of(LocalDateTime startDate, LocalDateTime endDate) {
        return new CafeRentalDateResponse(
                startDate.getYear(),
                startDate.getMonthValue(),
                startDate.getDayOfMonth(),
                endDate.getYear(),
                endDate.getMonthValue(),
                endDate.getDayOfMonth()
        );
    }

    public static CafeRentalDateResponse of(LocalDateTime date) {
        return of(date, date);
    }

    public static List<CafeRentalDateResponse> fromSchedules(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> CafeRentalDateResponse.of(schedule.getStartDate(), schedule.getEndDate()))
                .collect(Collectors.toList());
    }

    public static List<CafeRentalDateResponse> fromDayOffDates(List<LocalDateTime> dayOffDates) {
        return dayOffDates.stream()
                .map(CafeRentalDateResponse::of)
                .collect(Collectors.toList());
    }

    public static List<CafeRentalDateResponse> from(List<Schedule> schedules, List<LocalDateTime> dayOffDates) {
        List<CafeRentalDateResponse> responses = fromSchedules(schedules);
        responses.addAll(fromDayOffDates(dayOffDates));
        return responses;
    }
}
