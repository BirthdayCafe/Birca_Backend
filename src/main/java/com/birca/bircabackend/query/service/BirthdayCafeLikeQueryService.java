package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.BirthdayCafeLikeResponse;
import com.birca.bircabackend.query.repository.BirthdayCafeLikeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BirthdayCafeLikeQueryService {

    private final BirthdayCafeLikeQueryRepository birthdayCafeLikeQueryRepository;

    public List<BirthdayCafeLikeResponse> findBirthdayCafeLikes(Long visitantId) {
        return birthdayCafeLikeQueryRepository.findBirthdayCafeLikes(visitantId)
                .stream()
                .map(BirthdayCafeLikeResponse::from)
                .toList();
    }
}
