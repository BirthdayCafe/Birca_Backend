package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.query.service.BirthdayCafeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeQueryController {

    private final BirthdayCafeQueryService birthdayCafeQueryService;

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}/special-goods")
    @RequiredLogin
    public ResponseEntity<List<SpecialGoodsResponse>> findSpecialGoods(@PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findSpecialGoods(birthdayCafeId));
    }

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}/menus")
    @RequiredLogin
    public ResponseEntity<List<MenuResponse>> findMenus(@PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findMenus(birthdayCafeId));
    }

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}/lucky-draws")
    @RequiredLogin
    public ResponseEntity<List<LuckyDrawResponse>> findLuckyDraws(@PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findLuckyDraws(birthdayCafeId));
    }

    @GetMapping("/v1/birthday-cafes/me")
    @RequiredLogin
    public ResponseEntity<List<MyBirthdayCafeResponse>> findMyBirthdayCafes(LoginMember loginMember) {
        return ResponseEntity.ok(birthdayCafeQueryService.findMyBirthdayCafes(loginMember));
    }

    @GetMapping("/v1/birthday-cafes")
    @RequiredLogin
    public ResponseEntity<List<BirthdayCafeResponse>> findBirthdayCafes(@ModelAttribute BirthdayCafeParams birthdayCafeParams,
                                                                        @ModelAttribute PagingParams pagingParams,
                                                                        LoginMember loginMember) {
        return ResponseEntity.ok(birthdayCafeQueryService.findBirthdayCafes(birthdayCafeParams, pagingParams, loginMember));
    }

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}")
    @RequiredLogin
    public ResponseEntity<BirthdayCafeDetailResponse> findBirthdayCafeDetail(LoginMember loginMember, @PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findBirthdayCafeDetail(loginMember, birthdayCafeId));
    }
}
