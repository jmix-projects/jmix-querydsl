package io.jmix.querydsl.domain;

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.Entity;

@JmixEntity
@Entity
public class Novel extends Book {

    private static final long serialVersionUID = 4711598115423737544L;

}
