package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.query.dto.BirthdayCafeParams;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BirthdayCafeDynamicRepositoryImpl implements BirthdayCafeDynamicRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<BirthdayCafeView> findByParams(BirthdayCafeParams birthdayCafeParams,
                                               PagingParams pagingParams) {
        return null;
    }
}
