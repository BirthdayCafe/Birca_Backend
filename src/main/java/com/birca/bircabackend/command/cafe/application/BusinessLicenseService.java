package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import com.birca.bircabackend.command.cafe.domain.BusinessLicenseCode;
import com.birca.bircabackend.command.cafe.domain.BusinessLicenseRepository;
import com.birca.bircabackend.command.cafe.dto.BusinessLicenseCreateRequest;
import com.birca.bircabackend.common.exception.BusinessException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.DUPLICATE_BUSINESS_LICENSE_NUMBER;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessLicenseService {

    private final BusinessLicenseRepository businessLicenseRepository;

    public void saveBusinessLicense(LoginMember loginMember, BusinessLicenseCreateRequest request) {
        BusinessLicenseCode businessLicenseCode = BusinessLicenseCode.from(request.businessLicenseNumber());
        checkDuplicateBusinessLicenseCode(businessLicenseCode);
        BusinessLicense businessLicense = request.toBusinessLicense(loginMember, businessLicenseCode);
        businessLicenseRepository.save(businessLicense);
    }

    private void checkDuplicateBusinessLicenseCode(BusinessLicenseCode businessLicenseCode) {
        if (businessLicenseRepository.existsByCode(businessLicenseCode)) {
            throw BusinessException.from(DUPLICATE_BUSINESS_LICENSE_NUMBER);
        }
    }
}
