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
 * The user object in the database. Has a unidirectional mapping
 * to the user's associated privileges.
 */
@Entity
@Table(name = "AVATAR", schema = "APP")
@Indexed
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AvatarDO {

    @SequenceGenerator(name = "Avatar_Gen", sequenceName = "AVATAR_SEQ", allocationSize = 1, initialValue = 1)
    @Id
    @GeneratedValue(generator = "Avatar_Gen")
    private Long id;

    @Column(name = "USER_ID", unique = true)
    private Long userId;

    @Column(name = "MIME_TYPE", columnDefinition = "VARCHAR(64)")
    private String mimeType;

    @Lob
    @Column(name = "AVATAR", columnDefinition = "BLOB")
    private byte[] avatar;
}
