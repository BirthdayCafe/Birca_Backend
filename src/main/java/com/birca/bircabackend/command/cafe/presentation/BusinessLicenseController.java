package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseFacade;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BusinessLicenseController {

    private final BusinessLicenseFacade businessLicenseFacade;

    @PostMapping("/v1/cafes/license-read")
    @RequiredLogin
    public ResponseEntity<BusinessLicenseResponse> readBusinessLicense(LoginMember loginMember,
                                                                       @ModelAttribute MultipartFile businessLicense) {
        BusinessLicenseResponse response
                = businessLicenseFacade.getBusinessLicenseInfoAndUploadCount(loginMember, businessLicense);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/v1/cafes/apply")
    @RequiredLogin
    public ResponseEntity<Void> saveBusinessLicense(LoginMember loginMember,
                                                    @ModelAttribute BusinessLicenseCreateRequest request) {
        businessLicenseFacade.saveBusinessLicense(loginMember, request);
        return ResponseEntity.ok().build();
    }
}
