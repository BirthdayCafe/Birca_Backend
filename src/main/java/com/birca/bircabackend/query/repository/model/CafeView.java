package com.birca.bircabackend.query.repository.model;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.CafeImage;
import com.birca.bircabackend.command.like.domain.Like;

public record CafeView(
        Cafe cafe,
        CafeImage cafeImage,
        Like like
) {
}
