package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.auth.login.LoginMember;
import com.birca.bircabackend.command.auth.login.RequiredLogin;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseFacade;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseOcrService;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessLicenseController {

    private final BusinessLicenseFacade businessLicenseFacade;
    private final BusinessLicenseOcrService businessLicenseOcrService;

    @PostMapping("/v1/cafes/license-read")
    @RequiredLogin
    public ResponseEntity<BusinessLicenseResponse> readBusinessLicense(@ModelAttribute MultipartFile businessLicense) {
        BusinessLicenseResponse response = businessLicenseOcrService.readBusinessLicense(businessLicense);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/cafes/apply")
    @RequiredLogin
    public ResponseEntity<Void> saveBusinessLicense(LoginMember loginMember,
                                                    @ModelAttribute BusinessLicenseCreateRequest request) {
        businessLicenseFacade.save(loginMember, request);
        return ResponseEntity.ok().build();
    }
}
