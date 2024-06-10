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

    private static Boolean LIKED = false;

    private String name;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private Boolean liked = LIKED;
}
