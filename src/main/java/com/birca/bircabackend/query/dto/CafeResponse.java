package com.birca.bircabackend.query.dto;

import com.birca.bircabackend.command.cafe.domain.Cafe;

public record CafeResponse(Long cafeId, String cafeName) {

    public CafeResponse(Cafe cafe) {
        this(cafe.getId(), cafe.getName());
    }
}
