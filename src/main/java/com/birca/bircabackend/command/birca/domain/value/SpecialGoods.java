package com.birca.bircabackend.command.birca.domain.value;

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
public class SpecialGoods {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String details;

    public SpecialGoods(String name, String details) {
        this.name = name;
        this.details = details;
    }
}
