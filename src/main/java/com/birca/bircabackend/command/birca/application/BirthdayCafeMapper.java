package com.birca.bircabackend.command.birca.application;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.PhoneNumber;
import com.birca.bircabackend.command.birca.domain.value.Schedule;
import com.birca.bircabackend.command.birca.domain.value.Visitants;
import com.birca.bircabackend.command.birca.dto.ApplyRentalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BirthdayCafeMapper {

    private final BirthdayCafeRepository birthdayCafeRepository;

    public BirthdayCafe applyRental(ApplyRentalRequest request, Long hostId) {
        return BirthdayCafe.applyRental(
                hostId,
                request.artistId(),
                request.cafeId(),
                birthdayCafeRepository.findOwnerIdByCafeId(request.cafeId()),
                Schedule.of(request.startDate(), request.endDate()),
                Visitants.of(request.minimumVisitant(), request.maximumVisitant()),
                request.twitterAccount(),
                PhoneNumber.from(request.hostPhoneNumber())
        );
    }

    public BirthdayCafe addBirthDayCafe(ApplyRentalRequest request, Long hostId) {
        return BirthdayCafe.addBirthdayCafe(
                hostId,
                request.artistId(),
                request.cafeId(),
                birthdayCafeRepository.findOwnerIdByCafeId(request.cafeId()),
                Schedule.of(request.startDate(), request.endDate()),
                Visitants.of(request.minimumVisitant(), request.maximumVisitant()),
                request.twitterAccount(),
                PhoneNumber.from(request.hostPhoneNumber())
        );
    }
}
