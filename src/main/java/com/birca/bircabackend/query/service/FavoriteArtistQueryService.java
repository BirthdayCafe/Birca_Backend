package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.member.domain.Member;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.repository.FavoriteArtistQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.member.exception.MemberErrorCode.MEMBER_NOT_FOUND;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteArtistQueryService {

    private final FavoriteArtistQueryRepository favoriteArtistQueryRepository;
    private final EntityUtil entityUtil;

    public ArtistResponse findFavoriteArtist(LoginMember loginMember) {
        Member member = entityUtil.getEntity(Member.class, loginMember.id(), MEMBER_NOT_FOUND);
        return favoriteArtistQueryRepository.findFavoriteArtistByFanId(member.getId())
                .map(ArtistResponse::new)
                .orElseGet(ArtistResponse::createEmpty);
    }
}
