package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.query.service.CafeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeQueryController {

    private final CafeQueryService cafeQueryService;

    @GetMapping("/v1/cafes/me")
    @RequiredLogin
    public ResponseEntity<MyCafeDetailResponse> findMyCafeDetails(LoginMember loginMember) {
        return ResponseEntity.ok(cafeQueryService.findMyCafeDetails(loginMember));
    }

    @GetMapping("/v1/cafes")
    @RequiredLogin
    public ResponseEntity<List<CafeSearchResponse>> findCafes(LoginMember loginMember,
                                                              @ModelAttribute CafeParams cafeParams,
                                                              @ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(cafeQueryService.searchRentalAvailableCafes(loginMember, cafeParams, pagingParams));
    }

    @GetMapping("/v1/cafes/{cafeId}")
    @RequiredLogin
    public ResponseEntity<CafeDetailResponse> findCafeDetail(LoginMember loginMember,
                                                             @PathVariable Long cafeId) {
        return ResponseEntity.ok(cafeQueryService.findCafeDetail(loginMember, cafeId));
    }
}
