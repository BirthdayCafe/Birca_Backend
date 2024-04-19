package com.birca.bircabackend.query.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class CafeParams {
    private LocalDateTime startDate;
    private String startDay;
    private LocalDateTime endDate;
    private String endDay;
}
