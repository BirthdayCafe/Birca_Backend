package com.birca.bircabackend.query.controller;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.query.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.query.service.BusinessLicenseQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessLicenseQueryController {

    private final BusinessLicenseQueryService businessLicenseQueryService;

    @GetMapping("/v1/business-license/status")
    @RequiredLogin
    public ResponseEntity<BusinessLicenseStatusResponse> getBusinessLicenseStatus(LoginMember loginMember) {
        return ResponseEntity.ok(businessLicenseQueryService.getBusinessLicenseStatus(loginMember));
    }
}
