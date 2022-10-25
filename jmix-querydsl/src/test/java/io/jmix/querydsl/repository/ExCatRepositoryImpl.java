package io.jmix.querydsl.repository;

import io.jmix.core.DataManager;
import io.jmix.core.Metadata;
import io.jmix.core.entity.KeyValueEntity;
import io.jmix.querydsl.domain.CatBrowse;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

public class ExCatRepositoryImpl implements ExCatRepository {
    @Autowired
    private DataManager dataManager;
    @Autowired
    private Metadata metadata;

    @Override
    public List<CatBrowse> findCustomCats() {
        List<KeyValueEntity> loadValues = dataManager.loadValues("select c.name, c.birthdate, c.breed from querydslcuba_Cat c")
                .properties("name", "birthdate", "breed")
                .list();
        return loadValues.stream()
                .map(value -> new CatBrowse(value.getValue("name"), value.getValue("birthdate"), value.getValue("breed")))
                .collect(Collectors.toList());

    }
}
