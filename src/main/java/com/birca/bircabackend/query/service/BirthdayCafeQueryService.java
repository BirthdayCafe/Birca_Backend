package com.birca.bircabackend.query.service;

import com.birca.bircabackend.query.dto.SpecialGoodsResponse;
import com.birca.bircabackend.query.repository.BirthdayCafeQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BirthdayCafeQueryService {

    private final BirthdayCafeQueryRepository birthdayCafeQueryRepository;

    public List<SpecialGoodsResponse> findSpecialGoods(Long birthdayCafeId) {
        return birthdayCafeQueryRepository.findSpecialGoodsById(birthdayCafeId)
                .stream()
                .map(sg -> new SpecialGoodsResponse(sg.getName(), sg.getDetails()))
                .toList();
    }
}
