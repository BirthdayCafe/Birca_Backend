package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.DayOff;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface DayOffQueryRepository extends Repository<DayOff, Long> {

    @Query("select df.dayOffDate from DayOff df where df.cafeId = :cafeId and " +
            "YEAR(df.dayOffDate) = :year and MONTH(df.dayOffDate) = :month")
    List<LocalDateTime> findDayOffDateByCafeId(@Param("cafeId") Long cafeId, Integer year, Integer month);
}
