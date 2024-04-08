package com.birca.bircabackend.query.service;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.value.ProgressState;
import com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import com.birca.bircabackend.query.dto.*;
import com.birca.bircabackend.query.repository.BirthdayCafeImageQueryRepository;
import com.birca.bircabackend.query.repository.BirthdayCafeQueryRepository;
import com.birca.bircabackend.query.repository.CafeImageRepository;
import com.birca.bircabackend.query.repository.LikedBirthdayCafeQueryRepository;
import com.birca.bircabackend.query.repository.model.BirthdayCafeView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BirthdayCafeQueryService {

    private final BirthdayCafeQueryRepository birthdayCafeQueryRepository;
    private final BirthdayCafeImageQueryRepository birthdayCafeImageQueryRepository;
    private final LikedBirthdayCafeQueryRepository likedBirthdayCafeQueryRepository;
    private final CafeImageRepository cafeImageRepository;

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
        return birthdayCafeQueryRepository.findMyBirthdayCafes(loginMember.id())
                .stream()
                .map(MyBirthdayCafeResponse::from)
                .toList();
    }

    public List<BirthdayCafeResponse> findBirthdayCafes(BirthdayCafeParams birthdayCafeParams,
                                                        PagingParams pagingParams,
                                                        LoginMember loginMember) {
        return birthdayCafeQueryRepository.findByBirthdayCafes(loginMember.id(), birthdayCafeParams, pagingParams)
                .stream()
                .map(BirthdayCafeResponse::from)
                .toList();
    }

    public BirthdayCafeDetailResponse findBirthdayCafeDetail(LoginMember loginMember, Long birthdayCafeId) {
        BirthdayCafeView birthdayCafeView = birthdayCafeQueryRepository.findBirthdayCafeDetail(loginMember.id(), birthdayCafeId)
                .orElseThrow(() -> BusinessException.from(BirthdayCafeErrorCode.NOT_FOUND));
        List<String> defaultImages = birthdayCafeImageQueryRepository.findBirthdayCafeDefaultImages(birthdayCafeId);
        Integer likeCount = likedBirthdayCafeQueryRepository.findLikedCount(birthdayCafeId);
        List<String> cafeImages = cafeImageRepository.findByCafeId(birthdayCafeView.cafe().getId());
        return BirthdayCafeDetailResponse.of(birthdayCafeView, likeCount, defaultImages, cafeImages);
    }

    public List<BirthdayCafeApplicationResponse> findBirthdayCafeApplication(LoginMember loginMember, String progressState) {
        return birthdayCafeQueryRepository.findBirthdayCafeApplication(loginMember.id(), ProgressState.valueOf(progressState))
                .stream()
                .map(BirthdayCafeApplicationResponse::from)
                .toList();
    }

    public BirthdayCafeApplicationDetailResponse findBirthdayCafeApplicationDetail(LoginMember loginMember, Long birthdayCafeId) {
        BirthdayCafeView birthdayCafeView = birthdayCafeQueryRepository.findBirthdayCafeApplicationDetail(loginMember.id(), birthdayCafeId)
                .orElseThrow(() -> BusinessException.from(BirthdayCafeErrorCode.NOT_FOUND));
        return BirthdayCafeApplicationDetailResponse.from(birthdayCafeView);
    }
}
