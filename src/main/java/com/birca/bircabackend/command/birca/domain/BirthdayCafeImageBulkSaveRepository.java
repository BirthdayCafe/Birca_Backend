package com.birca.bircabackend.command.birca.domain;

import java.util.List;

public interface BirthdayCafeImageBulkSaveRepository {

    void saveAll(List<BirthdayCafeImage> birthdayCafeImages);
}
