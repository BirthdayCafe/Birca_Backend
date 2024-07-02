package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.query.dto.BirthdayCafeResponse;
import com.birca.bircabackend.query.dto.BirthdayCafeScheduleResponse;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BirthdayCafeQueryRepository extends Repository<BirthdayCafe, Long>, BirthdayCafeDynamicRepository {

    @Query("select bc.specialGoods from BirthdayCafe bc where bc.id = :id")
    List<SpecialGoods> findSpecialGoodsById(Long id);

    @Query("select bc.birthdayCafeMenus from BirthdayCafe bc where bc.id = :id")
    List<BirthdayCafeMenu> findMenusById(Long id);

    @Query("select bc.luckyDraws from BirthdayCafe bc where bc.id = :id")
    List<LuckyDraw> findLuckyDrawsById(Long id);

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, bci, a, ag) " +
            "from BirthdayCafe bc " +
            "join Artist a on a.id = bc.artistId " +
            "left join BirthdayCafeImage bci on bc.id = bci.birthdayCafeId and bci.isMain = true " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "where bc.hostId = :hostId " +
            "order by bc.id desc")
    List<BirthdayCafeView> findMyBirthdayCafes(@Param("hostId") Long hostId);

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, bci, a, ag, lk, c) " +
            "from BirthdayCafe bc " +
            "join Artist a on a.id = bc.artistId " +
            "left join BirthdayCafeImage bci on bc.id = bci.birthdayCafeId and bci.isMain = true " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "left join Like lk on lk.target.targetId = bc.id and lk.target.targetType = 'BIRTHDAY_CAFE' and lk.visitantId = :visitantId " +
            "left join Cafe c on c.id = bc.cafeId " +
            "where bc.id = :birthdayCafeId")
    Optional<BirthdayCafeView> findBirthdayCafeDetail(Long visitantId, Long birthdayCafeId);

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, a, ag, m) " +
            "from BirthdayCafe bc " +
            "join Artist a on a.id = bc.artistId " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "left join Member m on m.id = bc.hostId " +
            "where bc.cafeOwnerId = :ownerId and bc.progressState = :progressState")
    List<BirthdayCafeView> findBirthdayCafeApplication(Long ownerId, ProgressState progressState);

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, a, ag) " +
            "from BirthdayCafe bc " +
            "join Artist a on a.id = bc.artistId " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "where bc.cafeOwnerId = :ownerId and bc.id = :birthdayCafeId")
    Optional<BirthdayCafeView> findBirthdayCafeApplicationDetail(Long ownerId, Long birthdayCafeId);

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, a, ag, m) " +
            "from BirthdayCafe bc " +
            "join Artist a on a.id = bc.artistId " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "left join Member m on m.id = bc.hostId " +
            "where bc.schedule.startDate <= :date and bc.schedule.endDate >= :date " +
            "and bc.cafeOwnerId = :ownerId " +
            "and (bc.progressState != 'RENTAL_PENDING' and bc.progressState != 'RENTAL_CANCELED')")
    Optional<BirthdayCafeView> findBirthdayCafeScheduleDetail(Long ownerId, LocalDateTime date);

    @Query("select bc.schedule from BirthdayCafe bc " +
            "where YEAR(bc.schedule.startDate) = :year and MONTH(bc.schedule.startDate) = :month " +
            "and bc.cafeOwnerId = :ownerId " +
            "and (bc.progressState != 'RENTAL_PENDING' and bc.progressState != 'RENTAL_CANCELED')")
    List<Schedule> findBirthdayCafeSchedule(Long ownerId, Integer year, Integer month);

    @Query("select bc.schedule from BirthdayCafe bc where bc.cafeId = :cafeId and " +
            "YEAR(bc.schedule.startDate) = :year and MONTH(bc.schedule.startDate) = :month " +
            "and (bc.progressState != 'RENTAL_PENDING' and bc.progressState != 'RENTAL_CANCELED')")
    List<Schedule> findScheduleByCafeId(@Param("cafeId") Long cafeId, Integer year, Integer month);
}
