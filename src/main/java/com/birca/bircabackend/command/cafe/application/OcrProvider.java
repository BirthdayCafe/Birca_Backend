package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface OcrProvider {
    BusinessLicenseResponse getBusinessLicenseInfo(MultipartFile businessLicense);
}
