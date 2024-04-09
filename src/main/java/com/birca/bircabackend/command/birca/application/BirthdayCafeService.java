package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.*;
import com.birca.bircabackend.command.birca.dto.*;
import com.birca.bircabackend.common.EntityUtil;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static com.birca.bircabackend.command.birca.domain.value.ProgressState.*;
import static com.birca.bircabackend.command.birca.exception.BirthdayCafeErrorCode.*;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeService {

    private final BirthdayCafeRepository birthdayCafeRepository;
    private final EntityUtil entityUtil;
    private final BirthdayCafeMapper birthdayCafeMapper;

    public void applyRental(ApplyRentalRequest request, LoginMember loginMember) {
        Long hostId = loginMember.id();
        validateRentalPendingExists(hostId);
        BirthdayCafe birthdayCafe = birthdayCafeMapper.toBirthdayCafe(request, hostId);
        birthdayCafeRepository.save(birthdayCafe);
    }

    private void validateRentalPendingExists(Long hostId) {
        if (birthdayCafeRepository.existsByHostIdAndProgressState(hostId, RENTAL_PENDING)) {
            throw BusinessException.from(RENTAL_PENDING_EXISTS);
        }
    }

    public void cancelRental(Long birthdayCafeId, LoginMember loginMember) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        birthdayCafe.cancelRental(loginMember.id());
    }

    public void changeSpecialGoodsStockState(Long birthdayCafeId, LoginMember loginMember, StateChangeRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        SpecialGoodsStockState state = SpecialGoodsStockState.valueOf(request.state());
        birthdayCafe.changeState(state, loginMember.id());
    }

    public void changeCongestionState(Long birthdayCafeId, LoginMember loginMember, StateChangeRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        CongestionState state = CongestionState.valueOf(request.state());
        birthdayCafe.changeState(state, loginMember.id());
    }

    public void changeVisibility(Long birthdayCafeId, LoginMember loginMember, StateChangeRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        Visibility state = Visibility.valueOf(request.state());
        birthdayCafe.changeState(state, loginMember.id());
    }

    public void replaceSpecialGoods(Long birthdayCafeId,
                                    LoginMember loginMember,
                                    List<SpecialGoodsRequest> request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        List<SpecialGoods> specialGoods = request.stream()
                .map(req -> new SpecialGoods(req.name(), req.details()))
                .toList();
        birthdayCafe.replaceSpecialGoods(loginMember.id(), specialGoods);
    }

    public void replaceMenus(Long birthdayCafeId,
                             LoginMember loginMember,
                             List<MenuRequest> request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        List<BirthdayCafeMenu> birthdayCafeMenus = request.stream()
                .map(req -> BirthdayCafeMenu.of(req.name(), req.details(), req.price()))
                .toList();
        birthdayCafe.replaceMenus(loginMember.id(), birthdayCafeMenus);
    }

    public void replaceLuckyDraws(Long birthdayCafeId,
                                  LoginMember loginMember,
                                  List<LuckyDrawRequest> request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        List<LuckyDraw> luckyDraws = request.stream()
                .map(req -> new LuckyDraw(req.rank(), req.prize()))
                .toList();
        birthdayCafe.replaceLuckyDraws(loginMember.id(), luckyDraws);
    }

    public void updateBirthdayCafe(Long birthdayCafeId,
                                   LoginMember loginMember,
                                   BirthdayCafeUpdateRequest request) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        Long memberId = loginMember.id();
        birthdayCafe.updateName(memberId, request.birthdayCafeName());
        birthdayCafe.updateTwitterAccount(memberId, request.birthdayCafeTwitterAccount());
    }

    public void approveBirthdayCafeApplication(Long birthdayCafeId, LoginMember loginMember) {
        BirthdayCafe birthdayCafe = entityUtil.getEntity(BirthdayCafe.class, birthdayCafeId, NOT_FOUND);
        LocalDateTime startDate = birthdayCafe.getSchedule().getStartDate();
        LocalDateTime endDate = birthdayCafe.getSchedule().getEndDate();
        if (birthdayCafeRepository.existsBirthdayCafe(startDate, endDate, birthdayCafe.getCafeId())) {
            throw BusinessException.from(RENTAL_ALREADY_EXISTS);
        }
        birthdayCafe.approveRental(loginMember.id());
    }

}
