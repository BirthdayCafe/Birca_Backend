package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.query.service.CafeQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeQueryController {

    private final CafeQueryService cafeQueryService;

    @GetMapping("/v1/cafes/search")
    public ResponseEntity<List<CafeResponse>> searchCafes(@RequestParam String name) {
        return ResponseEntity.ok(cafeQueryService.findCafes(name));
    }
}
