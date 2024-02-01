package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.ArtistGroup;
import com.birca.bircabackend.query.dto.PagingParams;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.birca.bircabackend.command.artist.domain.QArtistGroup.artistGroup;

@Repository
@RequiredArgsConstructor
public class ArtistGroupDynamicRepositoryImpl implements ArtistGroupDynamicRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ArtistGroup> queryPagedGroupsSortByName(PagingParams pagingParams) {
        Long cursor = pagingParams.getCursor();
        String cursorName = findCursorName(cursor);
        return queryFactory.selectFrom(artistGroup)
                .where(DynamicBooleanBuilder.builder()
                        .and(() -> artistGroup.name.eq(cursorName).and(artistGroup.id.gt(cursor)))
                        .or(() -> artistGroup.name.gt(cursorName))
                        .build()
                )
                .orderBy(artistGroup.name.asc(), artistGroup.id.asc())
                .limit(pagingParams.getSize())
                .fetch();
    }

    private String findCursorName(Long cursor) {
        return queryFactory.select(artistGroup.name)
                .from(artistGroup)
                .where(artistGroup.id.eq(cursor))
                .fetchOne();
    }
}
