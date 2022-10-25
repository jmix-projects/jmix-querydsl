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
import java.util.List;
import java.util.UUID;

/**
 * The Class Company.
 */
@SuppressWarnings("DataModelLocalizedMessageMissing")
@JmixEntity
@Entity(name = "querydslcuba_company")
@Table(name = "QUERYDSL_CUBA_COMPANY")
public class Company {

    private static final long serialVersionUID = - 8839345132351963702L;

    @Id
    @Column(name = "ID")
    @JmixGeneratedValue
    private UUID id;

    @Column(name = "INT_ID", unique = true)
    protected Integer intId;

    @Column(name = "NAME")
    protected String name;

    @Column(name = "OFFICIAL_NAME")
    protected String officialName;

    @Column(name = "RATING_ORDINAL")
    protected String ratingOrdinal;

    @Column(name = "RATING_STRING")
    protected String ratingString;

    @ManyToOne
    @JoinColumn(name = "EMPLOYEE_ID")
    protected Employee ceo;

    @OneToMany(mappedBy = "company")
    protected List<Department> departments;

    public static Company company() {
        Metadata metadata = AppBeans.get(Metadata.class);
        return metadata.create(Company.class);
    }

    public Integer getIntId() {
        return intId;
    }

    public void setIntId(Integer intId) {
        this.intId = intId;
    }

    public void setRatingOrdinal(CompanyRating ratingOrdinal) {
        this.ratingOrdinal = ratingOrdinal == null ? null : ratingOrdinal.getId();
    }

    public CompanyRating getRatingOrdinal() {
        return ratingOrdinal == null ? null : CompanyRating.fromId(ratingOrdinal);
    }

    public void setRatingString(CompanyRating ratingString) {
        this.ratingString = ratingString == null ? null : ratingString.getId();
    }

    public CompanyRating getRatingString() {
        return ratingString == null ? null : CompanyRating.fromId(ratingString);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }

    public Employee getCeo() {
        return ceo;
    }

    public void setCeo(Employee ceo) {
        this.ceo = ceo;
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public void setDepartments(List<Department> departments) {
        this.departments = departments;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

}
