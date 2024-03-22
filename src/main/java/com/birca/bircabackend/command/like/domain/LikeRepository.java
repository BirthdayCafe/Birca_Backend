package com.birca.bircabackend.command.like.domain;

import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface LikeRepository extends Repository<Like, Long> {

    void save(Like like);

    Optional<Like> findByVisitantIdAndTarget(Long visitantId, LikeTarget target);

    void delete(Like like);
}
