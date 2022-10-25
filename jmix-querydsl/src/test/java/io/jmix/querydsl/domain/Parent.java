package io.jmix.querydsl.domain;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@JmixEntity
@Entity(name = "Parent2")
public class Parent {

    @Id
    Integer id;

    @OneToMany(mappedBy = "parent")
    Set<Child> children;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
