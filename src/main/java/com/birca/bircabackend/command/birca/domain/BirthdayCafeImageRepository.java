package com.birca.bircabackend.command.birca.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface BirthdayCafeImageRepository extends Repository<BirthdayCafeImage, Long> {

    void save(BirthdayCafeImage birthdayCafeImage);

    @Query("select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId and bci.isMain = false")
    List<BirthdayCafeImage> findDefaultByBirthdayCafeId(Long birthdayCafeId);

    @Query("select bci from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId and bci.isMain = true")
    Optional<BirthdayCafeImage> findMainByBirthdayCafeId(Long birthdayCafeId);
}
