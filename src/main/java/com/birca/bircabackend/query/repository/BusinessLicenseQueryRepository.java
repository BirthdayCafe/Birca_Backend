package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface BusinessLicenseQueryRepository extends Repository<BusinessLicense, Long> {

    @Query("select bl.registrationApproved from BusinessLicense bl where bl.ownerId = :ownerId")
    Boolean isRegistrationApprovedByOwnerId(@Param("ownerId") Long ownerId);
}
