package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.login.RequiredLogin;
import com.birca.bircabackend.query.dto.ArtistGroupResponse;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.service.ArtistGroupQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtistQueryController {

    private final ArtistGroupQueryService artistGroupQueryService;

    @GetMapping("/v1/artist-groups")
    @RequiredLogin
    public ResponseEntity<List<ArtistGroupResponse>> getArtistGroups(@ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(artistGroupQueryService.findGroups(pagingParams));
    }
}
