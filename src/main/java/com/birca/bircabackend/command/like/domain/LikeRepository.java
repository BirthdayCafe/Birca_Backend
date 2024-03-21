package com.birca.bircabackend.command.like.domain;

import org.springframework.data.repository.Repository;

public interface LikeRepository extends Repository<Like, Long> {

    void save(Like like);
}
