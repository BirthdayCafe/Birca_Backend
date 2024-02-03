package com.birca.bircabackend.command.cafe.infrastructure;

import com.birca.bircabackend.command.cafe.dto.BusinessLicenseResponse;
import org.springframework.web.multipart.MultipartFile;

public interface BusinessLicenseOcrClient {
    BusinessLicenseResponse readBusinessLicense(MultipartFile businessLicense);
}
