package com.birca.bircabackend.common;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.InternalServerErrorCode;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.http.ResponseEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseExtractor {

    public static <T> T getBody(ResponseEntity<T> response) {
        validateResponse(response);
        return response.getBody();
    }

    private static <T> void validateResponse(ResponseEntity<T> response) {
        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw BusinessException.from(new InternalServerErrorCode("api 호출에 문제가 생겼습니다."));
        }
    }
}
