package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.ArtistGroupResponse;
import com.birca.bircabackend.query.dto.ArtistResponse;
import com.birca.bircabackend.query.dto.ArtistSearchResponse;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.service.ArtistGroupQueryService;
import com.birca.bircabackend.query.service.ArtistQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtistQueryController {

    private final ArtistGroupQueryService artistGroupQueryService;
    private final ArtistQueryService artistQueryService;

    @GetMapping("/v1/artist-groups")
    @RequiredLogin
    public ResponseEntity<List<ArtistGroupResponse>> getArtistGroups(@ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(artistGroupQueryService.findGroups(pagingParams));
    }

    @GetMapping("/v1/artist-groups/{groupId}/artists")
    @RequiredLogin
    public ResponseEntity<List<ArtistResponse>> getArtistsOfGroup(@PathVariable(name = "groupId") Long groupId) {
        return ResponseEntity.ok(artistQueryService.findArtistByGroup(groupId));
    }

    @GetMapping("/v1/artists/solo")
    @RequiredLogin
    public ResponseEntity<List<ArtistResponse>> getSoloArtists(@ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(artistQueryService.findSoloArtists(pagingParams));
    }

    @GetMapping("/v1/artists/search")
    @RequiredLogin
    public ResponseEntity<List<ArtistSearchResponse>> searchArtist(@RequestParam String name) {
        return ResponseEntity.ok(artistQueryService.searchArtist(name));
    }
}
