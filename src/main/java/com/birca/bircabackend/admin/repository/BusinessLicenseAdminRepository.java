package com.birca.bircabackend.admin.repository;

import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

public interface BusinessLicenseAdminRepository extends Repository<BusinessLicense, Long> {

    @Modifying
    @Query("update BusinessLicense bl set bl.registrationApproved = true where bl.id = :businessLicenseId")
    void approveBusinessLicense(@Param("businessLicenseId") Long businessLicenseId);
}
