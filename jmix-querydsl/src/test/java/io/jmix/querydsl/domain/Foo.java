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

import io.jmix.core.metamodel.annotation.JmixEntity;

import javax.persistence.*;

/**
 * The Class Foo.
 */
@JmixEntity
@Entity
@Table(name = "foo_")
public class Foo {
    @Column(name = "BAR")
    public String bar;

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    public Integer id;


    @Temporal(TemporalType.DATE)
    @Column(name = "date")
    public java.util.Date startDate;

    public void setBar(String bar) {
        this.bar = bar;
    }

    public String getBar() {
        return bar;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
