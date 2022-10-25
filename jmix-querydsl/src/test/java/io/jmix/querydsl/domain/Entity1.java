package io.jmix.querydsl.domain;



import io.jmix.core.Metadata;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.querydsl.AppBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.UUID;

@JmixEntity
@Table(name = "QUERYDSL_CUBA_ENTITY1")
@Entity(name = "querydslcuba_Entity1")
public class Entity1 {

    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Column(name = "INT_ID")
    protected Integer intId;

    @Column(name = "PROPERTY")
    public String property;

    public static Entity1 entity1() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Entity1.class);
    }

    public static Entity1 entity1(Integer intId) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Entity1 entity = metadata.create(Entity1.class);

        entity.setIntId(intId);

        return entity;
    }

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
