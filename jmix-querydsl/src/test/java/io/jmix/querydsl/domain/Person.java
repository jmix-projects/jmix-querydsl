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

import com.querydsl.core.annotations.QueryInit;
import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * The Class Person.
 */
@SuppressWarnings("serial")
@JmixEntity
@Entity
@Table(name = "person_")
public class Person implements Serializable {
    @Temporal(TemporalType.DATE)
    @Column(name = "birthDay")
    java.util.Date birthDay;

    @Id
    Long i;

    @ManyToOne
    PersonId pid;

    @Column(name = "NAME")
    String name;

    @ManyToOne
    @QueryInit("calendar")
    Nationality nationality;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Long getI() {
        return i;
    }

    public void setI(Long id) {
        this.i = id;
    }
}
