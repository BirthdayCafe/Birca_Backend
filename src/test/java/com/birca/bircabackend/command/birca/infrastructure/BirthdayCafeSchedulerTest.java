package com.birca.bircabackend.command.birca.infrastructure;

import com.birca.bircabackend.command.birca.domain.BirthdayCafe;
import com.birca.bircabackend.command.birca.domain.BirthdayCafeRepository;
import com.birca.bircabackend.command.birca.domain.value.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;

class BirthdayCafeSchedulerTest {

    @Mock
    private BirthdayCafeRepository birthdayCafeRepository;

    @InjectMocks
    private BirthdayCafeScheduler birthdayCafeScheduler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 생일_카페_스케줄링_로직이_동작되는지_테스트한다() {
        // given
        BirthdayCafe birthdayCafe = mock(BirthdayCafe.class);
        List<BirthdayCafe> birthdayCafes = List.of(birthdayCafe);
        Schedule schedule = Schedule.of(LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        when(birthdayCafe.getSchedule()).thenReturn(schedule);
        when(birthdayCafeRepository.findAll()).thenReturn(birthdayCafes);

        // when
        birthdayCafeScheduler.updateBirthdayCafeProgressStates();

        // then
        verify(birthdayCafe, times(1)).changeState(schedule);
    }
}
