package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

public interface MemoRepository extends Repository<Memo, Long> {

    void save(Memo memo);

    void deleteByBirthdayCafeId(Long birthdayCafeId);
}
