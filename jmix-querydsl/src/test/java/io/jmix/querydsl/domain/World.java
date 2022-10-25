package io.jmix.querydsl.domain;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Set;

@JmixEntity
@Entity
public class World {

    @Id
    Long id;

    @OneToMany
    Set<Mammal> mammals;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
