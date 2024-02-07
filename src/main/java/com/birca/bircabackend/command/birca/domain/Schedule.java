package com.birca.bircabackend.command.birca.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class Schedule {

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    public static Schedule of(LocalDateTime startDate, LocalDateTime endDate) {
        return new Schedule(startDate, endDate);
    }
}
