package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.repository.Repository;

public interface DayOffRepository extends Repository<DayOff, Long> {

    void save(DayOff dayOff);

    void deleteByCafeId(Long cafeId);
}
