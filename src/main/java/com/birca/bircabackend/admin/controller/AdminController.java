package com.birca.bircabackend.admin.controller;

import com.birca.bircabackend.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/v1/approve/{businessLicenseId}")
    public ResponseEntity<Void> approveBusinessLicense(@PathVariable Long businessLicenseId) {
        adminService.approveBusinessLicense(businessLicenseId);
        return ResponseEntity.ok().build();
    }
}
