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
package org.kathrynhuxtable.heroes.service;

import java.util.Optional;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.User;
import org.kathrynhuxtable.heroes.service.persistence.UserDAO;
import org.kathrynhuxtable.heroes.service.persistence.domain.PrivilegeDO;
import org.kathrynhuxtable.heroes.service.persistence.domain.UserDO;

/**
 * The User Service. Provides an API for finding users and updating their preferred theme.
 * (No other updates are currently available.)
 */
@Slf4j
@Component
@AllArgsConstructor
public class UserService {

    private final UserDAO userDao;

    /**
     * Find a User by id.
     *
     * @param id the id to match.
     * @return the User matching the id, or {@code null} if no match.
     */
    public User findUser(long id) {
        Optional<UserDO> user = userDao.findById(id);
        return user.map(this::toUser).orElse(null);
    }

    /**
     * Update the user's preferred theme in the database.
     *
     * @param id the id to match.
     * @param theme the new preferred theme.
     * @return the saved User, or {@code null} if no match.
     */
    public User updateTheme(long id, String theme) {
        Optional<UserDO> optUser = userDao.findById(id);
        if (optUser.isEmpty()) {
            return null;
        } else {
            UserDO user = optUser.get();
            user.setPreferredTheme(theme);
            return toUser(userDao.save(user));
        }
    }

    /**
     * Convert a domain object to a transfer object.
     *
     * @param user a UserDO object.
     * @return a User object.
     */
    private User toUser(UserDO user) {
        return User.builder()
                .id(user.getUserId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .preferredTheme(user.getPreferredTheme())
                .privileges(user.getPrivileges().stream().map(PrivilegeDO::getPrivilege).toList())
                .build();
    }
}
