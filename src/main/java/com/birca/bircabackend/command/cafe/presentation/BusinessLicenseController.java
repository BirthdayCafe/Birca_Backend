package com.birca.bircabackend.command.cafe.presentation;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.auth.authorization.RequiredLogin;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseFacade;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseProcessingService;
import com.birca.bircabackend.command.cafe.application.BusinessLicenseService;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import com.birca.bircabackend.command.cafe.validation.UploadCountCheck;
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
    private final BusinessLicenseService businessLicenseService;
    private final BusinessLicenseProcessingService businessLicenseProcessingService;

    @PostMapping("/v1/cafes/license-read")
    @RequiredLogin
    @UploadCountCheck
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
        businessLicenseProcessingService.verifyBusinessLicenseStatus(request.businessLicenseNumber());
        businessLicenseService.saveBusinessLicense(loginMember, request);
        return ResponseEntity.ok().build();
    }
}
