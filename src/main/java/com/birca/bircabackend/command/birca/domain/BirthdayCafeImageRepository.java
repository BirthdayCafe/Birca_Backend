package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface BirthdayCafeImageRepository extends Repository<BirthdayCafeImage, Long> {

    void save(BirthdayCafeImage birthdayCafeImage);

    List<BirthdayCafeImage> findByBirthdayCafeId(Long birthdayCafeId);
}
