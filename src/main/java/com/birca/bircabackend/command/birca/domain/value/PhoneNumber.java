package com.birca.bircabackend.command.birca.domain.value;

import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.regex.Pattern;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@EqualsAndHashCode
public class PhoneNumber {

    private static final Pattern VALID_PATTERN = Pattern.compile("^\\d{3}-\\d{3,4}-\\d{4}$");

    @Column(name = "host_phone_number", nullable = false)
    private String value;

    public static PhoneNumber from(String value) {
        return new PhoneNumber(value);
    }

    private PhoneNumber(String value) {
        validateInvalidPattern(value);
        this.value = value;
    }

    private void validateInvalidPattern(String value) {
        if (!VALID_PATTERN.matcher(value).matches()) {
            throw BusinessException.from(BirthdayCafeErrorCode.INVALID_PHONE_NUMBER);
        }
    }
}
