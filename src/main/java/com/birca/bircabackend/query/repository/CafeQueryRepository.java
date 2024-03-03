package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CafeQueryRepository extends Repository<Cafe, Long> {

    @Query("SELECT c FROM Cafe c WHERE c.name like %:name%")
    List<Cafe> findByName(String name);
}
