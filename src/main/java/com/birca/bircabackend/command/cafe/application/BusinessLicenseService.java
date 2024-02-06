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
import org.springframework.web.multipart.MultipartFile;

import static com.birca.bircabackend.command.cafe.exception.BusinessLicenseErrorCode.DUPLICATE_BUSINESS_LICENSE_NUMBER;

@Service
@Transactional
@RequiredArgsConstructor
public class BusinessLicenseService {

    private final BusinessLicenseRepository businessLicenseRepository;

    public void saveBusinessLicense(LoginMember loginMember, BusinessLicenseCreateRequest request) {
        Long ownerId = loginMember.id();
        String owner = request.owner();
        String cafeName = request.cafeName();
        String businessLicenseNumber = request.businessLicenseNumber();
        String address = request.address();
        MultipartFile businessLicenseImage = request.businessLicense();

        BusinessLicenseCode businessLicenseCode = createBusinessLicenseCode(businessLicenseNumber);
        checkDuplicateBusinessLicenseCode(businessLicenseCode);

        BusinessLicense businessLicense = BusinessLicense.createBusinessLicense(ownerId, owner, cafeName,
                businessLicenseCode, address, businessLicenseImage.getOriginalFilename());

        businessLicenseRepository.save(businessLicense);
    }


    private void checkDuplicateBusinessLicenseCode(BusinessLicenseCode businessLicenseCode) {
        if (businessLicenseRepository.existsByCode(businessLicenseCode)) {
            throw BusinessException.from(DUPLICATE_BUSINESS_LICENSE_NUMBER);
        }
    }

    private BusinessLicenseCode createBusinessLicenseCode(String businessLicenseNumber) {
        String[] codes = businessLicenseNumber.split("-");
        Integer taxOfficeCode = Integer.parseInt(codes[0]);
        Integer businessTypeCode = Integer.parseInt(codes[1]);
        Integer serialCode = Integer.parseInt(codes[2]);
        return BusinessLicenseCode.createBusinessLicenseCode(taxOfficeCode, businessTypeCode, serialCode);
    }
}
