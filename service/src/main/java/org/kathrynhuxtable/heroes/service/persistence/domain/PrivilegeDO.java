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
 * The privilege object in the database.
 */
@Entity
@Table(name = "PRIVILEGE", schema = "APP")
@Indexed
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(value = PrivilegeKey.class)
public class PrivilegeDO {

    @Id
    @Column(name = "USER_ID")
    private Long userId;

    @Id
    @Column(name = "PRIVILEGE", columnDefinition = "VARCHAR(64)")
    private String privilege;
}
