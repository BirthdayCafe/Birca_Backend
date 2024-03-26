package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.birca.domain.BirthdayCafeImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BirthdayCafeImageQueryRepository extends Repository<BirthdayCafeImage, Long> {

    @Query("select bci.imageUrl from BirthdayCafeImage bci where bci.birthdayCafeId = :birthdayCafeId and bci.isMain = false")
    List<String> findBirthdayCafeDefaultImages(Long birthdayCafeId);
}
