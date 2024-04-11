package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.CafeImage;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CafeImageRepository extends Repository<CafeImage, Long> {

    void save(CafeImage cafeImage);

    @Query("select ci.imageUrl from CafeImage ci where ci.cafeId = :cafeId")
    List<String> findByCafeId(Long cafeId);

    void deleteByImageUrl(String imageUrl);
}
