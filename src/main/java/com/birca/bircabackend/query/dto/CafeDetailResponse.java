package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.cafe.domain.Cafe;

import java.time.LocalDateTime;
import java.util.List;

public record CafeDetailResponse(
        String name,
        String twitterAccount,
        String address,
        String businessHours,
        List<RentalScheduleResponse> rentalSchedules
) {

    public static CafeDetailResponse from(Cafe cafe, List<BirthdayCafe> birthdayCafes) {
        return new CafeDetailResponse(
                cafe.getName(),
                cafe.getTwitterAccount(),
                cafe.getAddress(),
                cafe.getBusinessHours(),
                birthdayCafes.stream()
                        .map(bc -> new RentalScheduleResponse(bc.getSchedule().getStartDate(), bc.getSchedule().getEndDate()))
                        .toList()
        );
    }

    public record RentalScheduleResponse(
            LocalDateTime startDate,
            LocalDateTime endDate
    ) {
    }
}
