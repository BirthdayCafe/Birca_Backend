package com.birca.bircabackend.command.birca.presentation;

import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.birca.application.MemoService;
import com.birca.bircabackend.command.birca.dto.MemoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemoController {

    private final MemoService memoService;

    @PostMapping("/v1/{birthdayCafeId}/memo")
    @RequiredLogin
    public ResponseEntity<Void> saveMemo(@PathVariable Long birthdayCafeId, @RequestBody MemoRequest request) {
        memoService.save(birthdayCafeId, request.content());
        return ResponseEntity.ok().build();
    }
}
