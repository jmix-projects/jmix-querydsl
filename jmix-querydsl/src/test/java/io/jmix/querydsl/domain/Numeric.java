package io.jmix.querydsl.domain;

import io.jmix.core.Metadata;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.querydsl.AppBeans;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.UUID;

@JmixEntity
@Table(name = "QUERYDSL_CUBA_NUMERIC")
@Entity(name = "querydslcuba_Numeric")
public class Numeric {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Column(name = "LONG_ID")
    private Long longId;

    @Column(name = "VALUE")
    private BigDecimal value;

    public static Numeric numeric() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Numeric.class);
    }

    public Long getLongId() {
        return longId;
    }

    public void setLongId
            (Long longId) {
        this.longId = longId;
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
