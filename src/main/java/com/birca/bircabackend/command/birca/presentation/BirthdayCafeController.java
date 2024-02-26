package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.BirthdayCafeService;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
