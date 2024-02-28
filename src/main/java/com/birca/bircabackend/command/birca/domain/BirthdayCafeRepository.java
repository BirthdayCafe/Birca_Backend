package com.birca.bircabackend.command.birca.domain;

import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface BirthdayCafeRepository extends Repository<BirthdayCafe, Long> {

    void save(BirthdayCafe birthdayCafe);

    Boolean existsByHostIdAndProgressState(Long hostId, ProgressState progressState);

    @Query("select c.ownerId from Cafe c where c.id = :cafeId")
    Long findOwnerIdByCafeId(@Param("cafeId") Long cafeId);
}
