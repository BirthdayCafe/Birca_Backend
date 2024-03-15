package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

import java.util.List;

public interface BirthdayCafeImageRepository extends Repository<BirthdayCafeImage, Long> {

    List<BirthdayCafeImage> findByBirthdayCafeId(Long birthdayCafeId);
}
