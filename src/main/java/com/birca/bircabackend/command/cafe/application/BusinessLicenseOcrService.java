package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class BusinessLicenseOcrService {

    private final OcrProvider ocrProvider;

    public BusinessLicenseResponse getBusinessLicenseInfo(MultipartFile businessLicense) {
        return ocrProvider.getBusinessLicenseInfo(businessLicense);
    }
}
