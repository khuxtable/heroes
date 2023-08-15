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
import org.springframework.web.bind.annotation.*;

import org.kathrynhuxtable.heroes.service.UserService;
import org.kathrynhuxtable.heroes.service.bean.User;

/**
 * User Service.
 */
@Slf4j
@Controller
@RequestMapping("user")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping(path = "/{id}", produces = "application/json")
	public @ResponseBody User find(@PathVariable int id) {
		return userService.findUser(id);
	}

	@GetMapping(path = "/username/{username}", produces = "application/json")
	public @ResponseBody User findByUsername(@PathVariable String username) {
		return userService.findUserByUsername(username);
	}

	@GetMapping(path = "/updateTheme/{id}", produces = "application/json")
	public @ResponseBody User login(@PathVariable int id,
	                                @RequestParam(name = "theme") String theme) {
		return userService.updateTheme(id, theme);
	}
}