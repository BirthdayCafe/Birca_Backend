package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.like.domain.LikeTargetType;
import com.birca.bircabackend.command.member.domain.MemberRole;
import com.birca.bircabackend.command.member.domain.QMember;
import com.birca.bircabackend.query.dto.CafeParams;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.model.CafeView;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.birca.bircabackend.command.birca.domain.QBirthdayCafe.birthdayCafe;
import static com.birca.bircabackend.command.cafe.domain.QCafe.cafe;
import static com.birca.bircabackend.command.cafe.domain.QCafeImage.cafeImage;
import static com.birca.bircabackend.command.cafe.domain.QDayOff.dayOff;
import static com.birca.bircabackend.command.like.domain.QLike.like;
import static com.birca.bircabackend.command.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class CafeDynamicRepositoryImpl implements CafeDynamicRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CafeView> searchRentalAvailableCafes(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams) {
        return queryFactory.select(Projections.constructor(CafeView.class, cafe, cafeImage, like))
                .from(cafe)
                .join(cafeImage).on(cafeImage.id.eq(
                        JPAExpressions.select(cafeImage.id.min())
                                .from(cafeImage)
                                .where(cafeImage.cafeId.eq(cafe.id))
                ))
                .leftJoin(member).on(member.id.eq(cafe.ownerId))
                .leftJoin(like).on(cafe.id.eq(like.target.targetId)
                        .and(like.target.targetType.eq(LikeTargetType.CAFE))
                        .and(like.visitantId.eq(loginMember.id())))
                .where(member.role.ne(MemberRole.DELETED))
                .where(generateDynamicCondition(loginMember, cafeParams, pagingParams))
                .limit(pagingParams.getSize())
                .fetch();
    }

    private BooleanBuilder generateDynamicCondition(LoginMember loginMember, CafeParams cafeParams, PagingParams pagingParams) {
        Long cursor = pagingParams.getCursor();
        LocalDateTime startDate = cafeParams.getStartDate();
        LocalDateTime endDate = cafeParams.getEndDate();
        DynamicBooleanBuilder builder = DynamicBooleanBuilder.builder()
                .and(() -> cafe.id.gt(cursor))
                .and(() -> cafe.name.contains(cafeParams.getName()))
                .and(() -> cafe.id.notIn(
                        JPAExpressions.select(birthdayCafe.cafeId)
                                .from(birthdayCafe)
                                .where(birthdayCafe.schedule.startDate.loe(endDate)
                                        .and(birthdayCafe.schedule.endDate.goe(startDate))
                                        .and(birthdayCafe.progressState.eq(ProgressState.IN_PROGRESS)
                                                .or(birthdayCafe.progressState.eq(ProgressState.RENTAL_APPROVED))))
                ))
                .and(() -> cafe.id.notIn(
                        JPAExpressions.select(dayOff.cafeId)
                                .from(dayOff)
                                .where(dayOff.dayOffDate.loe(endDate)
                                        .and(dayOff.dayOffDate.goe(startDate)))
                ));

        if (cafeParams.getLiked()) {
            builder.and(() -> cafe.id.in(
                    JPAExpressions.select(like.target.targetId)
                            .from(like)
                            .where(like.visitantId.eq(loginMember.id())
                                    .and(like.target.targetType.eq(LikeTargetType.CAFE))))
            );
        }

        return builder.build();
    }
}
