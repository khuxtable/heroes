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
package org.kathrynhuxtable.heroes.resources.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import org.kathrynhuxtable.heroes.service.AuthService;
import org.kathrynhuxtable.heroes.service.bean.AuthInfo;
import org.kathrynhuxtable.heroes.service.bean.User;

/**
 * Authentication service. Generally, I think authentication should be externalized in
 * some sort of single sign-on system, but this is a toy project.
 */
@Slf4j
@Controller
@RequestMapping("auth")
@AllArgsConstructor
public class AuthController {

	private final AuthService authService;

	@PutMapping(path = "login")
	public @ResponseBody User login(@RequestBody AuthInfo authInfo) {
		// This should probably just return the userId value and have the client look up the user.
		return authService.authenticate(authInfo.getUsername(), authInfo.getPassword());
	}
}