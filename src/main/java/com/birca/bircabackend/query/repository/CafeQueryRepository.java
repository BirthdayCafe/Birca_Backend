package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.query.dto.CafeResponse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface CafeQueryRepository extends Repository<Cafe, Long> {

    @Query("SELECT new com.birca.bircabackend.query.dto.CafeResponse(c.id, c.name) FROM Cafe c WHERE c.name like %:name%")
    List<CafeResponse> findByName(String name);
}
