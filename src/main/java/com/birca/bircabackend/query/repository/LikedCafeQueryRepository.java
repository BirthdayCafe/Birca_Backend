package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.like.domain.Like;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface LikedCafeQueryRepository extends Repository<Like, Long> {

    @Query("select count(lk.id) > 0 " +
            "from Like lk " +
            "where lk.visitantId = :visitantId and lk.target.targetType = 'CAFE'")
    Boolean existsByVisitantIdAndTargetIsCafe(@Param("visitantId") Long visitantId);
}
