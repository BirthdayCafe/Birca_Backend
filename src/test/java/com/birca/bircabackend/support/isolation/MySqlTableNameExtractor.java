package com.birca.bircabackend.support.isolation;

import jakarta.persistence.EntityManager;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("ci")
class MySqlTableNameExtractor implements TableNameExtractor{

    private final EntityManager entityManager;

    public MySqlTableNameExtractor(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<String> getNames() {
        return entityManager.createNativeQuery("SHOW TABLES")
                .getResultList()
                .stream()
                .filter(name -> !name.equals("flyway_schema_history"))
                .toList();
    }
}
