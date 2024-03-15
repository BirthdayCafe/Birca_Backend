package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.LuckyDrawResponse;
import com.birca.bircabackend.query.dto.MenuResponse;
import com.birca.bircabackend.query.dto.SpecialGoodsResponse;
import com.birca.bircabackend.query.service.BirthdayCafeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeQueryController {

    private final BirthdayCafeQueryService birthdayCafeQueryService;

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}/special-goods")
    public ResponseEntity<List<SpecialGoodsResponse>> findSpecialGoods(@PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findSpecialGoods(birthdayCafeId));
    }

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}/menus")
    public ResponseEntity<List<MenuResponse>> findMenus(@PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findMenus(birthdayCafeId));
    }

    @GetMapping("/v1/birthday-cafes/{birthdayCafeId}/lucky-draws")
    public ResponseEntity<List<LuckyDrawResponse>> findLuckyDraws(@PathVariable Long birthdayCafeId) {
        return ResponseEntity.ok(birthdayCafeQueryService.findLuckyDraws(birthdayCafeId));
    }
}