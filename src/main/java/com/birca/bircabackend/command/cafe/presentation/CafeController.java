package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.cafe.application.CafeService;
import com.birca.bircabackend.command.cafe.dto.CafeMenuRequest;
import com.birca.bircabackend.command.cafe.dto.CafeOptionRequest;
import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.command.cafe.dto.DayOffCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeController {

    private final CafeService cafeService;

    @PatchMapping("/v1/cafes")
    @RequiredLogin
    public ResponseEntity<Void> updateCafe(LoginMember loginMember, @RequestBody CafeUpdateRequest request) {
        cafeService.update(loginMember, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/cafes/menus")
    @RequiredLogin
    public ResponseEntity<Void> updateCafeMenus(LoginMember loginMember, @RequestBody List<CafeMenuRequest> requests) {
        cafeService.updateCafeMenus(loginMember, requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/cafes/options")
    @RequiredLogin
    public ResponseEntity<Void> updateCafeOptions(LoginMember loginMember, @RequestBody List<CafeOptionRequest> requests) {
        cafeService.updateCafeOptions(loginMember, requests);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/cafes/{cafeId}/day-off")
    @RequiredLogin
    public ResponseEntity<Void> markDayOff(@PathVariable Long cafeId,
                                           LoginMember loginMember,
                                           @RequestBody DayOffCreateRequest request) {
        cafeService.markDayOff(cafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }
}
