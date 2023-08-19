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

import java.util.Base64;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.Avatar;
import org.kathrynhuxtable.heroes.service.persistence.AvatarDAO;
import org.kathrynhuxtable.heroes.service.persistence.domain.AvatarDO;

/**
 * The Avatar Service. Provides an API for finding avatars and eventually updating them.
 */
@Slf4j
@Component
@AllArgsConstructor
public class AvatarService {

	private final AvatarDAO avatarDao;

	/**
	 * Find an Avatar by userId.
	 *
	 * @param userId the userId to match.
	 * @return the Avatar matching the userIdd, or {@code null} if no match.
	 */
	public Avatar findAvatarByUserId(long userId) {
		return toAvatar(avatarDao.findByUserId(userId));
	}

	/**
	 * Convert a domain object to a transfer object.
	 *
	 * @param avatar an AvatarDO object.
	 * @return an Avatar object.
	 */
	private Avatar toAvatar(AvatarDO avatar) {
		if (avatar == null) {
			return null;
		} else {
			return Avatar.builder()
					.id(avatar.getId())
					.userId(avatar.getUserId())
					.mimeType(avatar.getMimeType())
					.avatar(Base64.getEncoder().encodeToString(avatar.getAvatar()))
					.build();
		}
	}
}
