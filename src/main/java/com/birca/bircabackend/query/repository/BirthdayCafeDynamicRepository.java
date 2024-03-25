package com.birca.bircabackend.query.repository;

import com.birca.bircabackend.query.dto.BirthdayCafeParams;
import com.birca.bircabackend.query.dto.PagingParams;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;

import java.util.List;

public interface BirthdayCafeDynamicRepository {

    List<BirthdayCafeView> findByBirthdayCafes(Long visitantId,
                                               BirthdayCafeParams birthdayCafeParams,
                                               PagingParams pagingParams);
}
