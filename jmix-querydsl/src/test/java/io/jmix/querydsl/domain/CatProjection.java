package io.jmix.querydsl.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CatProjection {
    private final UUID id;
    private final String name;
    private final List<Cat> kittens = new ArrayList<>();

    public CatProjection(String name, UUID id, List<Cat> k) {
        this.id = id;
        this.name = name;
        kittens.addAll(k);
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Cat> getKittens() {
        return kittens;
    }
}
