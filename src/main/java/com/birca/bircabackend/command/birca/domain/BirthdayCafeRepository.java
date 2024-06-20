package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BirthdayCafeRepository extends Repository<BirthdayCafe, Long> {

    void save(BirthdayCafe birthdayCafe);

    Boolean existsByHostIdAndProgressState(Long hostId, ProgressState progressState);

    @Query("select c.ownerId from Cafe c where c.id = :cafeId")
    Long findOwnerIdByCafeId(@Param("cafeId") Long cafeId);

    @Query("select count(*) > 0 from BirthdayCafe bc " +
            "where (bc.schedule.startDate <= :endDate or bc.schedule.endDate >= :startDate) " +
            "and bc.cafeId = :cafeId " +
            "and (bc.progressState = 'RENTAL_APPROVED' or bc.progressState = 'IN_PROGRESS')")
    Boolean existsBirthdayCafe(LocalDateTime startDate, LocalDateTime endDate, Long cafeId);

    @Query("select count(*) > 0 from BirthdayCafe bc " +
            "where bc.cafeOwnerId = :cafeOwnerId " +
            "and (bc.schedule.startDate <= :endDate and bc.schedule.endDate >= :startDate)")
    Boolean hasBookedBirthdayCafe(@Param("startDate") LocalDateTime startDate,
                                  @Param("endDate") LocalDateTime endDate,
                                  @Param("cafeOwnerId") Long cafeOwnerId);

    List<BirthdayCafe> findAll();
}
