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

import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import org.kathrynhuxtable.heroes.service.AvatarService;
import org.kathrynhuxtable.heroes.service.bean.Avatar;

/**
 * Avatar Service.
 */
@Slf4j
@Controller
@RequestMapping("avatar")
@AllArgsConstructor
public class AvatarController {

	private final AvatarService avatarService;

	@GetMapping(path = "/data/{userId}", produces = "application/json")
	public @ResponseBody Avatar findByUsername(@PathVariable Long userId) {
		return avatarService.findAvatarByUserId(userId);
	}

	@GetMapping(path = "/image/{userId}")
	public @ResponseBody ResponseEntity<byte[]> imageByUsername(@PathVariable Long userId) {
		Avatar avatar = avatarService.findAvatarByUserId(userId);
		MediaType contentType = MediaType.parseMediaType(avatar.getMimeType());
		return ResponseEntity.ok()
				.contentType(contentType)
				.body(Base64.getDecoder().decode(avatar.getAvatar()));

	}
}