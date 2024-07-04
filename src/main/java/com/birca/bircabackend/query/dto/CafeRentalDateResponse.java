package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.birca.domain.value.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public record CafeRentalDateResponse(
        Integer startYear,
        Integer startMonth,
        Integer startDay,
        Integer endYear,
        Integer endMonth,
        Integer endDay
) {

    public static List<CafeRentalDateResponse> from(List<Schedule> schedules, List<LocalDateTime> dayOffDates) {
        return Stream.concat(
                        fromSchedules(schedules).stream(),
                        fromDayOffDates(dayOffDates).stream()
                )
                .toList();
    }

    private static CafeRentalDateResponse createRentalDateResponse(LocalDateTime startDate, LocalDateTime endDate) {
        return new CafeRentalDateResponse(
                startDate.getYear(),
                startDate.getMonthValue(),
                startDate.getDayOfMonth(),
                endDate.getYear(),
                endDate.getMonthValue(),
                endDate.getDayOfMonth()
        );
    }

    private static CafeRentalDateResponse createDayOffResponse(LocalDateTime dayOffDate) {
        return new CafeRentalDateResponse(
                dayOffDate.getYear(),
                dayOffDate.getMonthValue(),
                dayOffDate.getDayOfMonth(),
                dayOffDate.getYear(),
                dayOffDate.getMonthValue(),
                dayOffDate.getDayOfMonth()
        );
    }

    private static List<CafeRentalDateResponse> fromSchedules(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> CafeRentalDateResponse.createRentalDateResponse(schedule.getStartDate(), schedule.getEndDate()))
                .toList();
    }

    private static List<CafeRentalDateResponse> fromDayOffDates(List<LocalDateTime> dayOffDates) {
        return dayOffDates.stream()
                .map(CafeRentalDateResponse::createDayOffResponse)
                .toList();
    }
}
