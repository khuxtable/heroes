/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.kathrynhuxtable.heroes.service.persistence.domain;

import java.util.Date;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Indexed;

import org.kathrynhuxtable.heroes.service.persistence.UIFilterDescriptor;

/**
 * The Hero object in the database.
 */
@Entity
@Table(name = "HERO", schema = "APP")
@Indexed
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroDO {

    @SequenceGenerator(name = "Hero_Gen", sequenceName = "HERO_SEQ", allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(generator = "Hero_Gen")
    @Column(name = "ID")
    private Long id;

    @UIFilterDescriptor(global = true)
    @Column(name = "NAME", columnDefinition = "VARCHAR(128)")
    private String name;

    @UIFilterDescriptor(global = true)
    @Column(name = "POWER", columnDefinition = "VARCHAR(128)")
    private String power;

    @UIFilterDescriptor(global = true)
    @Column(name = "ALTER_EGO", columnDefinition = "VARCHAR(128)")
    private String alterEgo;

    @UIFilterDescriptor
    @Column(name = "RATING")
    private Integer rating;

    @UIFilterDescriptor
    @Column(name = "POWER_DATE")
    private Date powerDate;
}
