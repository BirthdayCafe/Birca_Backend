package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.BirthdayCafeLikeResponse;
import com.birca.bircabackend.query.repository.LikedBirthdayCafeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BirthdayCafeLikeQueryService {

    private final LikedBirthdayCafeQueryRepository likedBirthdayCafeQueryRepository;


    public List<BirthdayCafeLikeResponse> findLikedBirthdayCafes(Long visitantId) {
        return likedBirthdayCafeQueryRepository.findLikedBirthdayCafes(visitantId)
                .stream()
                .map(BirthdayCafeLikeResponse::from)
                .toList();
    }
}
