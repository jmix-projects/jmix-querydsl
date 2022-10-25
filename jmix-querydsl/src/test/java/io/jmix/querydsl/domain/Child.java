package io.jmix.querydsl.domain;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@JmixEntity
@Entity(name = "Child2")
public class Child {

    @Id
    Integer id;

    @ManyToOne
    Parent parent;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}
