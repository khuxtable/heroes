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

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Indexed;

/**
 * The login_info object in the database.
 */
@Entity
@Table(name = "LOGIN_INFO", schema = "APP")
@Indexed
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginInfoDO {

    @SequenceGenerator(name = "LoginInfo_Gen", sequenceName = "LOGIN_INFO_SEQ", allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(generator = "LoginInfo_Gen")
    @Column(name = "ID")
    private Long id;

    @Column(name = "USERNAME", unique = true, columnDefinition = "VARCHAR(32)")
    private String username;

    @Column(name = "PASSWORD", columnDefinition = "VARCHAR(128)")
    private String password;
}
