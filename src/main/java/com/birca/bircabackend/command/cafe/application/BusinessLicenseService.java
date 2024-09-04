package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import com.birca.bircabackend.command.cafe.domain.BusinessLicenseCode;
import com.birca.bircabackend.command.cafe.domain.BusinessLicenseRepository;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessLicenseService {

    private final BusinessLicenseRepository businessLicenseRepository;

    public void saveBusinessLicense(Long ownerId, BusinessLicenseCreateRequest request, String imageUrl) {
        BusinessLicenseCode businessLicenseCode = BusinessLicenseCode.from(request.businessLicenseNumber());
        BusinessLicense businessLicense = request.toBusinessLicense(ownerId, businessLicenseCode, imageUrl);
        businessLicenseRepository.save(businessLicense);
    }
}
