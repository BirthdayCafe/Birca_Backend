package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.like.domain.LikeTargetType;
import com.birca.bircabackend.query.dto.CafeParams;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.model.CafeView;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.birca.bircabackend.command.birca.domain.QBirthdayCafe.birthdayCafe;
import static com.birca.bircabackend.command.cafe.domain.QCafe.cafe;
import static com.birca.bircabackend.command.cafe.domain.QCafeImage.cafeImage;
import static com.birca.bircabackend.command.like.domain.QLike.like;

@Repository
@RequiredArgsConstructor
public class CafeDynamicRepositoryImpl implements CafeDynamicRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CafeView> searchCafes(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams) {
        return queryFactory.selectDistinct().select(Projections.constructor(CafeView.class, cafe, cafeImage, like))
                .from(cafe)
                .join(cafeImage).on(cafeImage.id.eq(
                        JPAExpressions.select(cafeImage.id.min())
                                .from(cafeImage)
                                .where(cafeImage.cafeId.eq(cafe.id))
                ))
                .leftJoin(like).on(
                        cafe.id.eq(like.target.targetId)
                                .and(like.target.targetType.eq(LikeTargetType.CAFE)))
//                .leftJoin(dayOff).on(dayOff.cafeId.eq(cafe.id)
//                        .and(DynamicBooleanBuilder.builder()
//                                .and(() -> dayOff.dayOffDate.notBetween(cafeParams.getStartDate(), cafeParams.getEndDate()))
//                                .build()))
                .leftJoin(birthdayCafe).on(birthdayCafe.cafeId.eq(cafe.id)
                        .and(DynamicBooleanBuilder.builder()
                                .and(() -> birthdayCafe.schedule.startDate.gt(cafeParams.getEndDate())
                                        .or(birthdayCafe.schedule.endDate.lt(cafeParams.getStartDate())))
                                .build()))
                .where(generateDynamicCondition(loginMember, cafeParams, pagingParams))
                .limit(pagingParams.getSize())
                .fetch();
    }

    private BooleanBuilder generateDynamicCondition(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams) {
        Long cursor = pagingParams.getCursor();
        DynamicBooleanBuilder builder = DynamicBooleanBuilder.builder()
                .and(() -> cafe.id.gt(cursor))
//                .and(() -> dayOff.dayOffDate.gt(cafeParams.getEndDate())
//                        .or(dayOff.dayOffDate.lt(cafeParams.getStartDate())))
                .and(() -> birthdayCafe.schedule.startDate.gt(cafeParams.getEndDate())
                        .or(birthdayCafe.schedule.endDate.lt(cafeParams.getStartDate())));

        if (cafeParams.getLiked()) {
            builder.and(() -> like.visitantId.eq(loginMember.id()));
        }

        return builder.build();
    }
}
