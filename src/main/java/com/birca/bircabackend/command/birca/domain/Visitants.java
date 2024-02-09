package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class Visitants {

    private Integer minimumVisitant;

    private Integer maximumVisitant;

    public static Visitants of(Integer minimumVisitant, Integer maximumVisitant) {
        return new Visitants(minimumVisitant, maximumVisitant);
    }

    public Visitants(Integer minimumVisitant, Integer maximumVisitant) {
        validateVisitants(minimumVisitant, maximumVisitant);
        this.minimumVisitant = minimumVisitant;
        this.maximumVisitant = maximumVisitant;
    }

    private void validateVisitants(Integer minimumVisitant, Integer maximumVisitant) {
        validateNotMinus(minimumVisitant);
        validateNotMinus(maximumVisitant);
        validateMaximumGreaterThanMinimum(minimumVisitant, maximumVisitant);
    }

    private void validateNotMinus(Integer visitant) {
        if (visitant != null && visitant <= 0) {
            throw BusinessException.from(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }
    }

    private void validateMaximumGreaterThanMinimum(Integer minimumVisitant, Integer maximumVisitant) {
        if (minimumVisitant != null && maximumVisitant != null && minimumVisitant > maximumVisitant) {
            throw BusinessException.from(BirthdayCafeErrorCode.INVALID_VISITANTS);
        }
    }
}
