package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.command.cafe.domain.BusinessLicense;
import org.springframework.data.repository.Repository;

public interface BusinessLicenseQueryRepository extends Repository<BusinessLicense, Long> {

    Boolean existsByOwnerId(Long ownerId);
}
