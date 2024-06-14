package com.birca.bircabackend.command.birca.infrastructure;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.Schedule;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Transactional
@RequiredArgsConstructor
public class BirthdayCafeScheduler {

    private static final String CRON_EXPRESSION = "0 0 0 * * *";

    private final BirthdayCafeRepository birthdayCafeRepository;

    @Scheduled(cron = CRON_EXPRESSION)
    public void updateBirthdayCafeProgressStates() {
        List<BirthdayCafe> birthdayCafes = birthdayCafeRepository.findAll();
        for (BirthdayCafe birthdayCafe : birthdayCafes) {
            Schedule schedule = birthdayCafe.getSchedule();
            birthdayCafe.changeState(schedule);
        }
    }
}
