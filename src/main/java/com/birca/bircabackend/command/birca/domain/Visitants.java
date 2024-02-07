package com.birca.bircabackend.command.birca.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Visitants {

    private Integer minimumVisitant;

    private Integer maximumVisitant;
}
