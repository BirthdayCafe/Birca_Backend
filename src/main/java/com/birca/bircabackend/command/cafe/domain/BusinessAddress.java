package com.birca.bircabackend.command.cafe.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class BusinessAddress {

    @Column(nullable = false, name = "address")
    private String value;

    @Column(nullable = false, name = "detail_address")
    private String detail;
}
