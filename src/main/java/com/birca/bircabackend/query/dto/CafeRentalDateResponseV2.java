package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.birca.domain.value.Schedule;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

public record CafeRentalDateResponseV2(
        Boolean isRentalDate,
        Integer startYear,
        Integer startMonth,
        Integer startDay,
        Integer endYear,
        Integer endMonth,
        Integer endDay
) {

    public static List<CafeRentalDateResponseV2> of(List<Schedule> schedules, List<LocalDateTime> dayOffDates) {
        return Stream.concat(
                        fromSchedules(schedules).stream(),
                        fromDayOffDates(dayOffDates).stream()
                )
                .toList();
    }

    private static CafeRentalDateResponseV2 createRentalDateResponse(LocalDateTime startDate, LocalDateTime endDate) {
        return new CafeRentalDateResponseV2(
                true,
                startDate.getYear(),
                startDate.getMonthValue(),
                startDate.getDayOfMonth(),
                endDate.getYear(),
                endDate.getMonthValue(),
                endDate.getDayOfMonth()
        );
    }

    private static CafeRentalDateResponseV2 createDayOffResponse(LocalDateTime dayOffDate) {
        return new CafeRentalDateResponseV2(
                false,
                dayOffDate.getYear(),
                dayOffDate.getMonthValue(),
                dayOffDate.getDayOfMonth(),
                dayOffDate.getYear(),
                dayOffDate.getMonthValue(),
                dayOffDate.getDayOfMonth()
        );
    }

    private static List<CafeRentalDateResponseV2> fromSchedules(List<Schedule> schedules) {
        return schedules.stream()
                .map(schedule -> CafeRentalDateResponseV2.createRentalDateResponse(schedule.getStartDate(), schedule.getEndDate()))
                .toList();
    }

    private static List<CafeRentalDateResponseV2> fromDayOffDates(List<LocalDateTime> dayOffDates) {
        return dayOffDates.stream()
                .map(CafeRentalDateResponseV2::createDayOffResponse)
                .toList();
    }
}
