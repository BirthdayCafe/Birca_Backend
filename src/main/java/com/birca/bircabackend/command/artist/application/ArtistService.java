package com.birca.bircabackend.command.artist.application;

import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.auth.login.LoginMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ArtistService {


    public void registerFavoriteArtist(FavoriteArtistRequest request,
                                       LoginMember loginMember) {

    }
}
