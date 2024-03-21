package com.birca.bircabackend.command.like.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface BirthdayCafeLikeRepository extends Repository<BirthdayCafeLike, Long> {

    void save(BirthdayCafeLike birthdayCafeLike);

    Optional<BirthdayCafeLike> findByVisitantIdAndBirthdayCafeId(Long visitantId, Long birthdayCafeId);

    void delete(BirthdayCafeLike birthdayCafeLike);
}
