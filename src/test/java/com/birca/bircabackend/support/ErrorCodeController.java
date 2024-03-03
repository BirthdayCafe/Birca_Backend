package com.birca.bircabackend.support;

import com.birca.bircabackend.common.exception.ErrorCode;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@RestController
@RequestMapping("/error-codes")
public class ErrorCodeController {

    @GetMapping
    public Map<Integer, Object> getErrorCode(@RequestParam(name = "className") String className) throws ClassNotFoundException {
        Class<?> errorCodeType = Class.forName(className);
        ErrorCode[] errorCodes = (ErrorCode[]) errorCodeType.getEnumConstants();
        return Arrays.stream(errorCodes)
                .collect(toMap(ErrorCode::getValue, ErrorCodeResponse::new));
    }

    record ErrorCodeResponse(
            Integer httpStatus,
            String message
    ) {
        public ErrorCodeResponse(ErrorCode code) {
            this(code.getHttpStatusCode(), code.getMessage());
        }
    }

}
