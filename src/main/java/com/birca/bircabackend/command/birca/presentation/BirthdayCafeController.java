package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.BirthdayCafeService;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PatchMapping("/v1/birthday-cafes/{birthdayCafeId}/{stateName}")
    @RequiredLogin
    public ResponseEntity<Void> changeState(@PathVariable Long birthdayCafeId,
                                            @PathVariable String stateName,
                                            LoginMember loginMember) {
        birthdayCafeService.changeState(birthdayCafeId, stateName, loginMember);
        return ResponseEntity.ok().build();
    }
}
