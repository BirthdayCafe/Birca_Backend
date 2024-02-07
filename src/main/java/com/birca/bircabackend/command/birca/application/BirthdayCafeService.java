package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.auth.authorization.LoginMember;
import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.dto.BirthdayCafeCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeService {

    private final BirthdayCafeRepository birthdayCafeRepository;

    public void createBirthdayCafe(BirthdayCafeCreateRequest request, LoginMember loginMember) {
        BirthdayCafe birthdayCafe = request.toEntity(loginMember.id());
        birthdayCafeRepository.save(birthdayCafe);
    }
}
