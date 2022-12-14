/*
 * Copyright 2015, The Querydsl Team (http://www.querydsl.com/team)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.jmix.querydsl.domain;

import io.jmix.core.Metadata;
import io.jmix.core.entity.annotation.JmixGeneratedValue;
import io.jmix.core.metamodel.annotation.JmixEntity;
import io.jmix.querydsl.AppBeans;

import javax.persistence.*;
import java.util.UUID;

/**
 * The Class Show.
 */
@JmixEntity
@Table(name = "QUERYDSL_CUBA_SHOW")
@Entity(name = "querydslcuba_Show")
public class Show {

    private static final long serialVersionUID = - 1107270705122358675L;

    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Column(name = "INT_ID", unique = true)
    protected Long lngId;

    @JoinColumn
    @ManyToOne
    public Show parent;

    public static Show show() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Show.class);
    }

    public static Show show(long id) {
        Metadata metadata = AppBeans.get(Metadata.class);
        Show show = metadata.create(Show.class);

        show.setLngId(id);

        return show;
    }

    public Long getLngId() {
        return lngId;
    }

    public void setLngId(Long lngId) {
        this.lngId = lngId;
    }

    public Show getParent() {
        return parent;
    }

    public void setParent(Show parent) {
        this.parent = parent;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
