package io.jmix.querydsl.domain;

import com.querydsl.core.annotations.QueryProjection;

import java.util.Date;

public class CatBrowse {
    private final String name;
    private final Date birthdate;
    private final Integer breed;

    @QueryProjection
    public CatBrowse(String name, Date birthdate, Integer breed) {
        this.name = name;
        this.birthdate = birthdate;
        this.breed = breed;
    }

    public String getName() {
        return name;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public Integer getBreed() {
        return breed;
    }
}
