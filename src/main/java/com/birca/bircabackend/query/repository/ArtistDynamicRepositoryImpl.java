package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.artist.domain.Artist;
import com.birca.bircabackend.query.dto.PagingParams;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.birca.bircabackend.command.artist.domain.QArtist.artist;

@Repository
@RequiredArgsConstructor
public class ArtistDynamicRepositoryImpl implements ArtistDynamicRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Artist> queryPagedSoloArtistsSortByName(PagingParams pagingParams) {
        Long cursor = pagingParams.getCursor();
        String cursorName = findCursorName(cursor);
        return queryFactory.selectFrom(artist)
                .where(DynamicBooleanBuilder.builder()
                        .and(() -> artist.groupId.isNull())
                        .and(() -> artist.name.eq(cursorName).and(artist.id.gt(cursor)).or(artist.name.gt(cursorName)))
                        .build()
                )
                .orderBy(artist.name.asc(), artist.id.asc())
                .limit(pagingParams.getSize())
                .fetch();
    }

    private String findCursorName(Long cursor) {
        return queryFactory.select(artist.name)
                .from(artist)
                .where(artist.id.eq(cursor))
                .fetchOne();
    }
}
