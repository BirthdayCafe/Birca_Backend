package com.birca.bircabackend.command.cafe.domain.value;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@Table(uniqueConstraints = {@UniqueConstraint(
        name = "uc_day_off_dayOffDate",
        columnNames = "dayOffDate"
)})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DayOff {

    @Column(nullable = false)
    private LocalDateTime dayOffDate;

    public DayOff(LocalDateTime dayOffDate) {
        this.dayOffDate = dayOffDate;
    }
}
