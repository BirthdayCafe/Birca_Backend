package com.birca.bircabackend.admin.repository;

import com.birca.bircabackend.command.cafe.domain.Cafe;
import org.springframework.data.repository.Repository;

public interface CafeAdminRepository extends Repository<Cafe, Long> {

    void save(Cafe cafe);
}
