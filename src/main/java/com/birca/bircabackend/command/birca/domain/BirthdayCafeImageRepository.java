package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface BirthdayCafeImageRepository extends Repository<BirthdayCafeImage, Long> {

    void save(BirthdayCafeImage birthdayCafeImage);

    List<BirthdayCafeImage> findDefaultByBirthdayCafeIdAndIsMain(Long birthdayCafeId, Boolean isMain);

    Optional<BirthdayCafeImage> findMainByBirthdayCafeIdAndIsMain(Long birthdayCafeId, Boolean isMain);
}
