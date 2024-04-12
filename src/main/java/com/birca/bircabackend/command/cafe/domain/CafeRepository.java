package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CafeRepository extends Repository<Cafe, Long> {

    Optional<Cafe> findByOwnerId(Long ownerId);
}
