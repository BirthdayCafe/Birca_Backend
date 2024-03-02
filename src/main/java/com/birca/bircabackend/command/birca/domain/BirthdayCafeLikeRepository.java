package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BirthdayCafeLikeRepository extends Repository<BirthdayCafeLike, Long> {

    void save(BirthdayCafeLike birthdayCafeLike);

    boolean existsByVisitantIdAndBirthdayCafeId(Long visitantId, Long birthdayCafeId);

    Optional<BirthdayCafeLike> findByVisitantIdAndBirthdayCafeId(Long visitantId, Long birthdayCafeId);

    void delete(BirthdayCafeLike birthdayCafeLike);
}
