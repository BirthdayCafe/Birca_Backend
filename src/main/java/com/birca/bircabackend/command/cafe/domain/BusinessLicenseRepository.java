package com.birca.bircabackend.command.cafe.domain;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;

public interface BusinessLicenseRepository extends Repository<BusinessLicense, Long> {

    void save(BusinessLicense businessLicense);

    boolean existsByCode(BusinessLicenseCode code);

    @Query("select count(*) > 0 " +
            "from BusinessLicense bl " +
            "where bl.ownerId = :ownerId and bl.registrationApproved = true")
    boolean existsByRegistrationApprovedAndOwnerId(Long ownerId);
}
