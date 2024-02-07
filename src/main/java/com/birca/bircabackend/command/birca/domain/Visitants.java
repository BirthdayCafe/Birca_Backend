package com.birca.bircabackend.command.birca.domain;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@EqualsAndHashCode
public class Visitants {

    private Integer minimumVisitant;

    private Integer maximumVisitant;

    public static Visitants of(Integer minimumVisitant, Integer maximumVisitant) {
        return new Visitants(minimumVisitant, maximumVisitant);
    }
}
