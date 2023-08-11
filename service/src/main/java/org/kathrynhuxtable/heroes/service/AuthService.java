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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.AuthInfo;
import org.kathrynhuxtable.heroes.service.bean.User;

@Slf4j
@Component
public class AuthService {

	private List<AuthInfo> users;

	@Autowired
	private SecurityService securityService;

	public AuthService() {
		users = new ArrayList<>();

		users.add(makeUser(1, "admin", "secret1"));
		users.add(makeUser(2, "user", "pass1"));
	}

	public User authenticate(String username, String password) {
		for (AuthInfo user : users) {
			if (user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return findUser(user.getUsername());
			}
		}
		return null;
	}

	public User findUser(String username) {
		for (AuthInfo user : users) {
			if (user.getUsername().equalsIgnoreCase(username)) {
				return securityService.findUser(user.getId());
			}
		}
		return null;
	}

	private AuthInfo makeUser(int id, String username, String password) {
		return AuthInfo.builder().id(id).username(username).password(password).build();
	}
}
