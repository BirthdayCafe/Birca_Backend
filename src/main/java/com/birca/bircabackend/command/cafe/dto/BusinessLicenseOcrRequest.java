package com.birca.bircabackend.command.cafe.dto;

import java.util.List;

public record BusinessLicenseOcrRequest(
        String version,
        String requestId,
        Long timestamp,
        List<ImageInfo> images
) {

    public record ImageInfo(
            String format,
            String data,
            String name
    ) {
    }
}
