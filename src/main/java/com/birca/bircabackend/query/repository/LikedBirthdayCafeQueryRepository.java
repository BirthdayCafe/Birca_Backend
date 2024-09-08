package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.like.domain.Like;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LikedBirthdayCafeQueryRepository extends Repository<Like, Long> {

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, bci, a, ag) " +
            "from Like lk " +
            "join BirthdayCafe bc on bc.id = lk.target.targetId and lk.target.targetType = 'BIRTHDAY_CAFE' " +
            "left join BirthdayCafeImage bci on bci.birthdayCafeId = bc.id and bci.isMain = true " +
            "left join Artist a on a.id = bc.artistId " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "where lk.visitantId = :visitantId")
    List<BirthdayCafeView> findLikedBirthdayCafes(@Param("visitantId") Long visitantId);

    @Query("select count(lk.id) " +
            "from Like lk " +
            "where lk.target.targetId = :birthdayCafeId and lk.target.targetType = 'BIRTHDAY_CAFE'")
    Integer findLikedCount(Long birthdayCafeId);
}
