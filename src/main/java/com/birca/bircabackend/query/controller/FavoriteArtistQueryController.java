package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.service.FavoriteArtistQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class FavoriteArtistQueryController {

    private final FavoriteArtistQueryService favoriteArtistQueryService;

    @GetMapping("/v1/artists/favorite")
    @RequiredLogin
    public ArtistResponse getFavoriteArtist(LoginMember loginMember) {
        return favoriteArtistQueryService.findFavoriteArtist(loginMember);
    }
}
