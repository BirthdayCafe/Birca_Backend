package com.birca.bircabackend.command.cafe.domain.value;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class CafeOption {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer price;

    public CafeOption(String name, Integer price) {
        this.name = name;
        this.price = price;
    }
}
