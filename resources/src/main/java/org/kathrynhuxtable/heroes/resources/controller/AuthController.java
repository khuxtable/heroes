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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import org.kathrynhuxtable.heroes.service.AuthService;
import org.kathrynhuxtable.heroes.service.persistence.domain.LoginInfoDO;

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

	@PostMapping(path = "login",
			consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String login(@RequestParam MultiValueMap<String, String> formData) {
		String username = formData.getFirst("username");
		String password = formData.getFirst("password");
		if (username == null || username.isBlank() || password == null || password.isBlank()) {
			return null;
		}
		LoginInfoDO loginInfo = authService.authenticate(username.trim(), password.trim());
		if (loginInfo == null) {
			return null;
		} else {
			return loginInfo.getUsername();
		}
	}
}