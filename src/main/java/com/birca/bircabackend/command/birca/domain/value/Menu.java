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
public class Menu {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String details;

    @Column(nullable = false)
    private Integer price;

    public static Menu of(String name, String details, Integer price) {
        return new Menu(name, details, price);
    }

    private Menu(String name, String details, Integer price) {
        this.name = name;
        this.details = details;
        this.price = price;
    }
}
