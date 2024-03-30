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
public class BirthdayCafeMenu {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private Integer price;

    public static BirthdayCafeMenu of(String name, String details, Integer price) {
        return new BirthdayCafeMenu(name, details, price);
    }

    private BirthdayCafeMenu(String name, String details, Integer price) {
        this.name = name;
        this.details = details;
        this.price = price;
    }
}
