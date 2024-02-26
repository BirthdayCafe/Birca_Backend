package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BirthdayCafeRepository extends Repository<BirthdayCafe, Long> {

    void save(BirthdayCafe birthdayCafe);

    Optional<BirthdayCafe> findByHostIdAndProgressState(Long hostId, ProgressState progressState);
}
