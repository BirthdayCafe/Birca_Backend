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
        return queryFactory.selectFrom(artistGroup)
                .orderBy(artistGroup.name.asc())
                .limit(pagingParams.getSize())
                .fetch();
    }
}
