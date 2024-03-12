package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.BirthdayCafeService;
import com.birca.bircabackend.command.birca.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeController {

    private final BirthdayCafeService birthdayCafeService;

    @PostMapping("/v1/birthday-cafes")
    @RequiredLogin
    public ResponseEntity<Void> registerBirthdayCafe(@RequestBody ApplyRentalRequest request,
                                                     LoginMember loginMember) {
        birthdayCafeService.applyRental(request, loginMember);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/cancel")
    @RequiredLogin
    public ResponseEntity<Void> cancelRental(@PathVariable Long birthdayCafeId, LoginMember loginMember) {
        birthdayCafeService.cancelRental(birthdayCafeId, loginMember);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/birthday-cafes/{birthdayCafeId}/specialGoods")
    @RequiredLogin
    public ResponseEntity<Void> changeSpecialGoodsStockState(@PathVariable Long birthdayCafeId,
                                                             LoginMember loginMember,
                                                             @RequestBody StateChangeRequest request) {
        birthdayCafeService.changeSpecialGoodsStockState(birthdayCafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/birthday-cafes/{birthdayCafeId}/congestion")
    @RequiredLogin
    public ResponseEntity<Void> changeCongestionState(@PathVariable Long birthdayCafeId,
                                                      LoginMember loginMember,
                                                      @RequestBody StateChangeRequest request) {
        birthdayCafeService.changeCongestionState(birthdayCafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/v1/birthday-cafes/{birthdayCafeId}/visibility")
    @RequiredLogin
    public ResponseEntity<Void> changeVisibilityState(@PathVariable Long birthdayCafeId,
                                                      LoginMember loginMember,
                                                      @RequestBody StateChangeRequest request) {
        birthdayCafeService.changeVisibility(birthdayCafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/special-goods")
    @RequiredLogin
    public ResponseEntity<Void> replaceSpecialGoods(@PathVariable Long birthdayCafeId,
                                                    LoginMember loginMember,
                                                    @RequestBody List<SpecialGoodsRequest> request) {
        birthdayCafeService.replaceSpecialGoods(birthdayCafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/menus")
    @RequiredLogin
    public ResponseEntity<Void> replaceMenus(@PathVariable Long birthdayCafeId,
                                             LoginMember loginMember,
                                             @RequestBody List<MenuRequest> request) {
        birthdayCafeService.replaceMenus(birthdayCafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/lucky-draws")
    @RequiredLogin
    public ResponseEntity<Void> replaceLuckyDraws(@PathVariable Long birthdayCafeId,
                                             LoginMember loginMember,
                                             @RequestBody List<LuckyDrawRequest> request) {
        birthdayCafeService.replaceLuckyDraws(birthdayCafeId, loginMember, request);
        return ResponseEntity.ok().build();
    }
}
