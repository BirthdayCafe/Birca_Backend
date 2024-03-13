package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.value.SpecialGoods;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface BirthdayCafeQueryRepository extends Repository<BirthdayCafe, Long> {

    @Query("select bc.specialGoods from BirthdayCafe bc where bc.id = :id")
    List<SpecialGoods> findSpecialGoodsById(Long id);
}
