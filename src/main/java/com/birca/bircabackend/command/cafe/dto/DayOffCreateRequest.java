package com.birca.bircabackend.command.cafe.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DayOffCreateRequest(List<LocalDateTime> datOffDates) {
}
