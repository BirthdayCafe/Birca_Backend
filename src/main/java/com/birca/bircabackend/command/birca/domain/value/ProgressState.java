package com.birca.bircabackend.command.birca.domain.value;

public enum ProgressState {

    RENTAL_PENDING,
    RENTAL_APPROVED,
    RENTAL_CANCELED,
    IN_PROGRESS,
    FINISHED;

    public boolean isRentalPending() {
        return this == RENTAL_PENDING;
    }

    public boolean isRentalCanceled() {
        return this == RENTAL_CANCELED;
    }

    public boolean isRentalApproved() {
        return this == RENTAL_APPROVED;
    }

    public boolean isInProgress() {
        return this == IN_PROGRESS;
    }
}
