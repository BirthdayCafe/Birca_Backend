package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

public interface BirthdayCafeRepository extends Repository<BirthdayCafe, Long> {

    void save(BirthdayCafe birthdayCafe);
}
