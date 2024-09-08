package com.birca.bircabackend.query.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
@EqualsAndHashCode
public class DateParams {

    private Integer year;
    private Integer month;
}
