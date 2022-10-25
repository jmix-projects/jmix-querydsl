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
import java.util.Collection;

/**
 * The Class Payment.
 */
@JmixEntity
@Entity
public class Payment extends Item {
    @ManyToOne
    Status currentStatus, status;

    @Enumerated
            @Column(name = "paymentStatus")
    PaymentStatus paymentStatus;

    @OneToMany
    Collection<StatusChange> statusChanges;
}