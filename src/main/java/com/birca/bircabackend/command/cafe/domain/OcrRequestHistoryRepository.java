package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OcrRequestHistoryRepository extends Repository<OcrRequestHistory, Long> {

    void save(OcrRequestHistory ocrRequestHistory);

    Optional<OcrRequestHistory> findByOwnerId(Long ownerId);

    @Query("SELECT o.uploadCount FROM OcrRequestHistory o WHERE o.ownerId = :ownerId")
    Optional<Integer> findUploadCountByOwnerId(@Param("ownerId") Long ownerId);
}
