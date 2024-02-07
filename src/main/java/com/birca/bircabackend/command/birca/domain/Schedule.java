package com.birca.bircabackend.command.birca.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Schedule {

    private LocalDateTime startDate;

    private LocalDateTime endDate;
}
