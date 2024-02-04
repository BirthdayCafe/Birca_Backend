package com.birca.bircabackend.command.artist.presentation;

import com.birca.bircabackend.command.artist.application.ArtistService;
import com.birca.bircabackend.command.artist.dto.FavoriteArtistRequest;
import com.birca.bircabackend.command.artist.dto.InterestArtistRequest;
import com.birca.bircabackend.command.auth.authentication.LoginMember;
import com.birca.bircabackend.command.auth.authentication.RequiredLogin;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @PostMapping("/v1/artists/interest")
    @RequiredLogin
    public ResponseEntity<Void> registerInterestArtist(@RequestBody List<InterestArtistRequest> request,
                                                       LoginMember loginMember) {
        artistService.registerInterestArtist(request, loginMember);
        return ResponseEntity.ok().build();
    }
}
