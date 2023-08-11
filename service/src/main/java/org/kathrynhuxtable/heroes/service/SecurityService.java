/*
 * Copyright 2002-2018 the original author or authors.
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

import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.User;

@Slf4j
@Component
public class SecurityService {

    private List<User> users;

    public SecurityService() {
        users = new ArrayList<>();

        users.add(makeUser(1, "Felonius", "Gru", "VIEW", "ADMIN"));
        users.add(makeUser(2, "Reed", "Richards", "VIEW"));
    }

    public List<User> findAllUsers() {
        return new ArrayList<>(users);
    }

    public User findUser(long id) {
        for (User user : users) {
            if (user.getId() == id) {
                return user;
            }
        }
        return null;
    }

    private User makeUser(int id, String firstName, String lastName, String... privileges) {
        User user = User.builder().id(id).firstName(firstName).lastName(lastName).build();
        user.setPrivileges(new ArrayList<>());
        if (privileges != null) {
            for (String p : privileges) {
                user.getPrivileges().add(p);
            }
        }
        return user;
    }
}
