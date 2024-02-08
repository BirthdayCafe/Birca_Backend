package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseInfoResponse;
import org.springframework.web.multipart.MultipartFile;

public interface OcrProvider {
    BusinessLicenseInfoResponse getBusinessLicenseInfo(MultipartFile businessLicense);
}
