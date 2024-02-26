package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Schedule {

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    public static Schedule of(LocalDateTime startDate, LocalDateTime endDate) {
        return new Schedule(startDate, endDate);
    }

    private Schedule(LocalDateTime startDate, LocalDateTime endDate) {
        validateSchedule(startDate, endDate);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    private void validateSchedule(LocalDateTime startDate, LocalDateTime endDate) {
        if (startDate != null && endDate != null && startDate.isAfter(endDate)) {
            throw BusinessException.from(BirthdayCafeErrorCode.INVALID_SCHEDULE);
        }
    }
}
