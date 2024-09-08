package com.birca.bircabackend.command.birca.dto;

public record MenuRequest(
        String name,
        String details,
        Integer price
) {
}
