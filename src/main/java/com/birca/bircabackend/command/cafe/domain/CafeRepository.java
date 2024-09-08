package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CafeRepository extends Repository<Cafe, Long> {

    Optional<Cafe> findByOwnerId(Long ownerId);

    @Query("select c.id from Cafe c where c.ownerId = :ownerId")
    Long findCafeIdByOwnerId(Long ownerId);
}
