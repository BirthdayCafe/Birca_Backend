package com.birca.bircabackend.command.like.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.like.application.CafeLikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CafeLikeController {

    private final CafeLikeService cafeLikeService;

    @PostMapping("/v1/cafes/{cafeId}/like")
    @RequiredLogin
    public ResponseEntity<Void> like(@PathVariable Long cafeId, LoginMember loginMember) {
        cafeLikeService.like(cafeId, loginMember);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/v1/cafes/{cafeId}/like")
    @RequiredLogin
    public ResponseEntity<Void> cancelLike(@PathVariable Long cafeId, LoginMember loginMember) {
        cafeLikeService.cancelLike(cafeId, loginMember);
        return ResponseEntity.ok().build();
    }
}
