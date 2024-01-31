package com.birca.bircabackend.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class PagingParams {

    private static Long DEFAULT_CURSOR = 0L;
    private static Integer DEFAULT_SIZE = 6;

    private Long cursor = DEFAULT_CURSOR;
    private Integer size = DEFAULT_SIZE;
}
