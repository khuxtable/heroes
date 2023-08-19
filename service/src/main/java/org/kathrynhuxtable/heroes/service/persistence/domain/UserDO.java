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

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.stereotype.Indexed;

/**
 * The user object in the database. Has a unidirectional mapping
 * to the user's associated privileges.
 */
@Entity
@Table(name = "USER_INFO", schema = "APP")
@Indexed
@Data
public class UserDO {

    @SequenceGenerator(name = "User_Gen", sequenceName = "USER_SEQ", allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(generator = "User_Gen")
    @Column(name = "USER_ID")
    private Long userId;

    @Column(name = "USERNAME", unique = true, columnDefinition = "VARCHAR(32)")
    private String username;

    @Column(name = "LAST_NAME", columnDefinition = "VARCHAR(128)")
    private String lastName;

    @Column(name = "FIRST_NAME", columnDefinition = "VARCHAR(128)")
    private String firstName;

    @Column(name = "PREFERRED_THEME", columnDefinition = "VARCHAR(128)")
    private String preferredTheme;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "USER_ID", referencedColumnName = "USER_ID")
    private Set<PrivilegeDO> privileges = new HashSet<>();
}
