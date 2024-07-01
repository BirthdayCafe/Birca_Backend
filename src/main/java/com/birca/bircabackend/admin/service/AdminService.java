package com.birca.bircabackend.admin.service;

import com.birca.bircabackend.admin.repository.BusinessLicenseAdminRepository;
import com.birca.bircabackend.admin.repository.CafeAdminRepository;
import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.common.EntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.BUSINESS_LICENSE_NOT_FOUND;

@Service
@Transactional
@RequiredArgsConstructor
public class AdminService {

    private final BusinessLicenseAdminRepository businessLicenseAdminRepository;
    private final CafeAdminRepository cafeAdminRepository;
    private final EntityUtil entityUtil;

    public void approveBusinessLicense(Long businessLicenseId) {
        BusinessLicense license = entityUtil.getEntity(BusinessLicense.class, businessLicenseId, BUSINESS_LICENSE_NOT_FOUND);
        businessLicenseAdminRepository.approveBusinessLicense(businessLicenseId);
        Cafe cafe = new Cafe(businessLicenseId, license.getOwnerId(), license.getCafeName(), license.getAddress());
        cafeAdminRepository.save(cafe);
    }
}
