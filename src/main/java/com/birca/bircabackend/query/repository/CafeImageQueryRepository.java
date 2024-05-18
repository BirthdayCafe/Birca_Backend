package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CafeImageQueryRepository extends Repository<CafeImage, Long> {

    @Query("select ci.imageUrl from CafeImage ci where ci.cafeId = :cafeId")
    List<String> findByCafeId(Long cafeId);
}
