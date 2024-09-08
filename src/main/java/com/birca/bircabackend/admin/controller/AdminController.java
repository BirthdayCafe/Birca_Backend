package com.birca.bircabackend.admin.controller;

import com.birca.bircabackend.admin.dto.AdminAuthRequest;
import com.birca.bircabackend.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping("/v1/approve/{businessLicenseId}")
    public ResponseEntity<Void> approveBusinessLicense(@PathVariable Long businessLicenseId,
                                                       @RequestBody AdminAuthRequest request) {
        adminService.approveBusinessLicense(businessLicenseId, request);
        return ResponseEntity.ok().build();
    }
}
