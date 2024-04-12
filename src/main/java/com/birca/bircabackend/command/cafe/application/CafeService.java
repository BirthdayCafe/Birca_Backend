package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.cafe.domain.Cafe;
import com.birca.bircabackend.command.cafe.domain.CafeRepository;
import com.birca.bircabackend.command.cafe.domain.value.CafeMenu;
import com.birca.bircabackend.command.cafe.domain.value.CafeOption;
import com.birca.bircabackend.command.cafe.dto.CafeUpdateRequest;
import com.birca.bircabackend.command.cafe.exception.CafeErrorCode;
import com.birca.bircabackend.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CafeService {

    private final CafeRepository cafeRepository;

    public void update(LoginMember loginMember, CafeUpdateRequest request) {
        Cafe cafe = cafeRepository.findByOwnerId(loginMember.id())
                .orElseThrow(() -> BusinessException.from(CafeErrorCode.NOT_FOUND));
        cafe.update(request.cafeName(), request.cafeAddress(),
                request.twitterAccount(), request.businessHours());
        replaceCafeMenus(request, cafe);
        replaceCafeOptions(request, cafe);
    }

    private void replaceCafeMenus(CafeUpdateRequest request, Cafe cafe) {
        List<CafeMenu> cafeMenus = request.cafeMenus().stream()
                .map(req -> new CafeMenu(req.name(), req.price()))
                .toList();
        cafe.replaceCafeMenus(cafeMenus);
    }

    private void replaceCafeOptions(CafeUpdateRequest request, Cafe cafe) {
        List<CafeOption> cafeOptions = request.cafeOptions().stream()
                .map(req -> new CafeOption(req.name(), req.price()))
                .toList();
        cafe.replaceCafeOptions(cafeOptions);
    }
}
