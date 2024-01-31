package com.birca.bircabackend.common;

import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.common.exception.ErrorCode;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class EntityUtil {

    private final EntityManager entityManager;

    public <T> T getEntity(Class<T> type, Long id, ErrorCode errorCode) {
        return Optional.ofNullable(entityManager.find(type, id))
                .orElseThrow(() -> BusinessException.from(errorCode));
    }
}
