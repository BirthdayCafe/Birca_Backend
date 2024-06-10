package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CafeQueryRepository extends Repository<Cafe, Long>, CafeDynamicRepository {

    Optional<Cafe> findByOwnerId(Long ownerId);
}
