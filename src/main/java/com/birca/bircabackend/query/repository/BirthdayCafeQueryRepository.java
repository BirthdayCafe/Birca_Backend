package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.LuckyDraw;
import com.birca.bircabackend.command.birca.domain.value.Menu;
import com.birca.bircabackend.command.birca.domain.value.SpecialGoods;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BirthdayCafeQueryRepository extends Repository<BirthdayCafe, Long>, BirthdayCafeDynamicRepository {

    @Query("select bc.specialGoods from BirthdayCafe bc where bc.id = :id")
    List<SpecialGoods> findSpecialGoodsById(Long id);

    @Query("select bc.menus from BirthdayCafe bc where bc.id = :id")
    List<Menu> findMenusById(Long id);

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

    @Query("select new com.birca.bircabackend.query.repository.model.BirthdayCafeView(bc, bci, a, ag, lk) " +
            "from BirthdayCafe bc " +
            "join Artist a on a.id = bc.artistId " +
            "left join BirthdayCafeImage bci on bc.id = bci.birthdayCafeId and bci.isMain = true " +
            "left join ArtistGroup ag on a.groupId = ag.id " +
            "left join Like lk on lk.target.targetId = bc.id and lk.target.targetType = 'BIRTHDAY_CAFE' and lk.visitantId = :visitantId " +
            "where bc.id = :birthdayCafeId")
    Optional<BirthdayCafeView> findBirthdayCafeDetail(Long visitantId, Long birthdayCafeId);
}
