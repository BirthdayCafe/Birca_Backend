package com.birca.bircabackend.command.like.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.like.application.BirthdayCafeLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BirthdayCafeLikeController {

    private final BirthdayCafeLikeService birthdayCafeLikeService;

    @PostMapping("/v1/birthday-cafes/{birthdayCafeId}/like")
    @RequiredLogin
    public ResponseEntity<Void> like(@PathVariable Long birthdayCafeId, LoginMember loginMember) {
        birthdayCafeLikeService.like(birthdayCafeId, loginMember);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1/birthday-cafes/{birthdayCafeId}/like")
    @RequiredLogin
    public ResponseEntity<Void> cancelLike(@PathVariable Long birthdayCafeId, LoginMember loginMember) {
        birthdayCafeLikeService.cancelLike(birthdayCafeId, loginMember);
        return ResponseEntity.ok().build();
    }
}
