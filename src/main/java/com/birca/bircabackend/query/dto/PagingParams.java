package com.birca.bircabackend.query.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
@Setter
public class PagingParams {

    private Long cursor;
    private Integer size;
}
