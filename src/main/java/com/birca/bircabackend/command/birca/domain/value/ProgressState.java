package com.birca.bircabackend.command.birca.domain.value;

public enum ProgressState {

    RENTAL_PENDING,
    RENTAL_APPROVED,
    RENTAL_CANCELED,
    IN_PROGRESS,
    FINISHED

    ;

    public boolean isRentalPending() {
        return this == RENTAL_PENDING;
    }
}
