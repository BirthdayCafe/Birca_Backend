package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.service.InterestArtistQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class InterestArtistQueryController {

    private final InterestArtistQueryService interestArtistQueryService;

    @GetMapping("/v1/artists/interest")
    @RequiredLogin
    public List<ArtistResponse> getInterestArtists(LoginMember loginMember) {
        return interestArtistQueryService.findInterestArtists(loginMember);
    }
}
