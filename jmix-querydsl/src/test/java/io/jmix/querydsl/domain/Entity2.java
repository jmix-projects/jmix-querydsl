package io.jmix.querydsl.domain;



import io.jmix.core.Metadata;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.querydsl.AppBeans;

import javax.persistence.Column;
import javax.persistence.Entity;

@JmixEntity
@Entity(name = "querydslcuba_Entity2")
public class Entity2 extends Entity1 {

    private static final long serialVersionUID = - 1796122460916396985L;

    @Column(name = "PROPERTY2")
    public String property2;

    public static Entity2 entity2() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Entity2.class);
    }

    public static Entity2 entity2(Integer intId) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Entity2 entity = metadata.create(Entity2.class);

        entity.setIntId(intId);

        return entity;
    }

}
