package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.like.domain.BirthdayCafeLike;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BirthdayCafeLikeQueryRepository extends Repository<BirthdayCafeLike, Long> {

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, bci, a, ag) " +
            "from BirthdayCafeLike bcl " +
            "join BirthdayCafe bc on bc.id = bcl.birthdayCafeId " +
            "left join BirthdayCafeImage bci on bci.birthdayCafeId = bc.id and bci.isMain = true " +
            "left join Artist a on a.id = bc.artistId " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "where bcl.visitantId = :visitantId")
    List<BirthdayCafeView> findLikedBirthdayCafes(@Param("visitantId") Long visitantId);
}
