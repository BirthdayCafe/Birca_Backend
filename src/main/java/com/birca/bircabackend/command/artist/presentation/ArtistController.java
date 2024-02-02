package com.birca.bircabackend.command.artist.presentation;

import com.birca.bircabackend.command.artist.application.ArtistService;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.auth.login.RequiredLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @PostMapping("/v1/artists/favorite")
    @RequiredLogin
    public ResponseEntity<Void> registerFavoriteArtist(@RequestBody FavoriteArtistRequest request,
                                                       LoginMember loginMember) {
        artistService.registerFavoriteArtist(request, loginMember);
        return ResponseEntity.ok().build();
    }
}
