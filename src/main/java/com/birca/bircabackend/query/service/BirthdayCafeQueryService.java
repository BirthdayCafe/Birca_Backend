package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.query.dto.LuckyDrawResponse;
import com.birca.bircabackend.query.dto.MenuResponse;
import com.birca.bircabackend.query.dto.MyBirthdayCafeResponse;
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

    public List<MenuResponse> findMenus(Long birthdayCafeId) {
        return birthdayCafeQueryRepository.findMenusById(birthdayCafeId)
                .stream()
                .map(menu -> new MenuResponse(menu.getName(), menu.getDetails(), menu.getPrice()))
                .toList();
    }

    public List<LuckyDrawResponse> findLuckyDraws(Long birthdayCafeId) {
        return birthdayCafeQueryRepository.findLuckyDrawsById(birthdayCafeId)
                .stream()
                .map(luckyDraw -> new LuckyDrawResponse(luckyDraw.getRank(), luckyDraw.getPrize()))
                .toList();
    }

    public List<MyBirthdayCafeResponse> findMyBirthdayCafes(LoginMember loginMember) {
        return null;
    }
}
