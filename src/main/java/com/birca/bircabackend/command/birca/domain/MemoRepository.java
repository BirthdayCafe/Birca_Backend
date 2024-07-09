package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemoRepository extends Repository<Memo, Long> {

    void save(Memo memo);

    Optional<Memo> findByBirthdayCafeId(Long birthdayCafeId);
}
