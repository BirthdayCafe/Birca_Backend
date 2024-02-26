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
public class PhoneNumber {

    @Column(name = "host_phone_number", nullable = false)
    private String value;

    public static PhoneNumber from(String value) {
        return new PhoneNumber(value);
    }

    private PhoneNumber(String value) {
        this.value = value;
    }
}
