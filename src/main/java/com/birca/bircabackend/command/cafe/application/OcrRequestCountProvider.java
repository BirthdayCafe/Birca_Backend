package com.birca.bircabackend.command.cafe.application;

import com.birca.bircabackend.command.cafe.dto.UploadCountResponse;

public interface OcrRequestCountProvider {

    UploadCountResponse getUploadCount(Long ownerId);
}
