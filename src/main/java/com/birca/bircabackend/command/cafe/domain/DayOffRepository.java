package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DayOffRepository extends Repository<DayOff, Long> {

    void save(DayOff dayOff);

    void deleteByCafeIdAndDayOffDate(Long cafeId, LocalDateTime dayOffDate);

    @Query("select do.dayOffDate from DayOff do where do.cafeId = :cafeId")
    List<LocalDateTime> findByCafeId(Long cafeId);

    @Query("select count(*) > 0 from DayOff do " +
            "where (do.dayOffDate <= :endDate and do.dayOffDate >= :startDate) " +
            "and do.cafeId = :cafeId")
    Boolean existsByDayOffDate(@Param("startDate") LocalDateTime startDate,
                               @Param("endDate") LocalDateTime endDate,
                               @Param("cafeId") Long cafeId);
}
