package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.repository.Repository;

public interface BusinessLicenseRepository extends Repository<BusinessLicense, Long> {

    void save(BusinessLicense businessLicense);

    boolean existsByCode(BusinessLicenseCode code);
}
