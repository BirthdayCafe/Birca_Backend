package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.domain.value.Visibility;
import com.birca.bircabackend.command.like.domain.LikeTargetType;
import com.birca.bircabackend.query.dto.BirthdayCafeParams;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.birca.bircabackend.command.artist.domain.QArtist.artist;
import static com.birca.bircabackend.command.artist.domain.QArtistGroup.artistGroup;
import static com.birca.bircabackend.command.birca.domain.QBirthdayCafe.birthdayCafe;
import static com.birca.bircabackend.command.birca.domain.QBirthdayCafeImage.birthdayCafeImage;
import static com.birca.bircabackend.command.cafe.domain.QCafe.cafe;
import static com.birca.bircabackend.command.like.domain.QLike.like;

@Repository
@RequiredArgsConstructor
public class BirthdayCafeDynamicRepositoryImpl implements BirthdayCafeDynamicRepository {


    private final JPAQueryFactory queryFactory;

    @Override
    public List<BirthdayCafeView> findByBirthdayCafes(Long visitantId,
                                                      BirthdayCafeParams birthdayCafeParams,
                                                      PagingParams pagingParams) {
        return queryFactory.select(Projections.constructor(BirthdayCafeView.class,
                        birthdayCafe, birthdayCafeImage, artist, artistGroup, like, cafe))
                .from(birthdayCafe)
                .join(artist).on(birthdayCafe.artistId.eq(artist.id))
                .leftJoin(cafe).on(cafe.id.eq(birthdayCafe.cafeId))
                .leftJoin(artistGroup).on(artistGroup.id.eq(artist.groupId))
                .leftJoin(birthdayCafeImage).on(
                        birthdayCafe.id.eq(birthdayCafeImage.birthdayCafeId)
                                .and(birthdayCafeImage.isMain.isTrue())
                )
                .leftJoin(like).on(
                        birthdayCafe.id.eq(like.target.targetId)
                                .and(like.target.targetType.eq(LikeTargetType.BIRTHDAY_CAFE))
                                .and(like.visitantId.eq(visitantId))
                )
                .where(birthdayCafe.visibility.eq(Visibility.PUBLIC))
                .where(generateDynamicCondition(birthdayCafeParams, pagingParams))
                .orderBy(birthdayCafe.schedule.startDate.asc(), birthdayCafe.id.asc())
                .limit(pagingParams.getSize())
                .fetch();
    }

    private BooleanBuilder generateDynamicCondition(BirthdayCafeParams birthdayCafeParams,
                                                    PagingParams pagingParams) {
        Long cursor = pagingParams.getCursor();
        LocalDateTime cursorStartDate = findCursorStartDate(cursor);
        return DynamicBooleanBuilder.builder()
                .and(() -> birthdayCafe.progressState.eq(ProgressState.valueOf(birthdayCafeParams.getProgressState())))
                .and(() -> birthdayCafe.artistId.eq(birthdayCafeParams.getArtistId()))
                .and(() -> birthdayCafe.cafeId.eq(birthdayCafeParams.getCafeId()))
                .and(() -> birthdayCafe.schedule.startDate.eq(cursorStartDate).and(birthdayCafe.id.gt(cursor))
                        .or(birthdayCafe.schedule.startDate.gt(cursorStartDate)))
                .build();
    }

    private LocalDateTime findCursorStartDate(Long cursor) {
        return queryFactory.select(birthdayCafe.schedule.startDate)
                .from(birthdayCafe)
                .where(birthdayCafe.id.eq(cursor))
                .fetchOne();
    }
}
