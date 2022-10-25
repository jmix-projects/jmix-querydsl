package io.jmix.querydsl.repository;

import io.jmix.querydsl.domain.CatBrowse;

import java.util.List;

public interface ExCatRepository {
    List<CatBrowse> findCustomCats();
}
