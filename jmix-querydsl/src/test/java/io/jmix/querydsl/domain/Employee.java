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
 * The Class Employee.
 */
@JmixEntity
@Table(name = "QUERYDSL_CUBA_EMPLOYEE")
@Entity(name = "querydslcuba_Employee")
public class Employee {

    private static final long serialVersionUID = - 607899760147703707L;

    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Column(name = "INT_ID")
    protected Integer intId;

    @Column(name = "FIRST_NAME")
    protected String firstName;

    @Column(name = "LAST_NAME")
    protected String lastName;

    @ManyToOne
    @JoinColumn(name = "COMPANY_ID")
    protected Company company;

    @OneToOne
    @JoinColumn(name = "USER_ID")
    protected User user;


    public static Employee employee() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Employee.class);
    }

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
