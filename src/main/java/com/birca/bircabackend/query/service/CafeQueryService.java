package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.CafeResponse;
import com.birca.bircabackend.query.repository.CafeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CafeQueryService {

    private final CafeQueryRepository cafeQueryRepository;

    public List<CafeResponse> findCafes(String name) {
        return cafeQueryRepository.findByName(name);
    }
}
