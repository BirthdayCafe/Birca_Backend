package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.BusinessLicenseStatusResponse;
import com.birca.bircabackend.query.repository.BusinessLicenseQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BusinessLicenseQueryService {

    private final BusinessLicenseQueryRepository businessLicenseQueryRepository;

    public BusinessLicenseStatusResponse getBusinessLicenseStatus(LoginMember loginMember) {
        Boolean registrationApproved = businessLicenseQueryRepository.existsByOwnerId(loginMember.id());
        return new BusinessLicenseStatusResponse(registrationApproved);
    }
}
