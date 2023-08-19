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
package org.kathrynhuxtable.heroes.createdb;

import java.io.IOException;
import java.io.InputStream;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.kathrynhuxtable.heroes.service.persistence.*;
import org.kathrynhuxtable.heroes.service.persistence.domain.*;
import org.kathrynhuxtable.heroes.service.persistence.util.DerbySlf4jBridge;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class Application implements CommandLineRunner {

	private final ApplicationProperties applicationProperties;
	private final LoginInfoDAO loginInfoDao;
	private final PrivilegeDAO privilegeDao;
	private final AvatarDAO avatarDAO;
	private final UserDAO userDao;
	private final HeroDAO heroDao;

	public static void main(String[] args) {
		try {
			// Required to direct Derby logs into Slf4J
			System.setProperty("derby.stream.error.method", DerbySlf4jBridge.getMethodName());

			new SpringApplication(Application.class).run();
		} catch (Exception e) {
			e.printStackTrace(System.out);
		} finally {
			System.exit(0);
		}
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Deleting old data");
		deleteAllData();
		log.info("Old data deleted");

		log.info("Initializing Logins");
		initializeLogins();
		log.info("Initialized Logins");

		log.info("Initializing Users");
		initializeUsers();
		log.info("Initialized Users");

		log.info("Initializing Heroes");
		initializeHeroes();
		log.info("Initialized Heroes");

	}

	private void deleteAllData() {
		loginInfoDao.deleteAll();
		privilegeDao.deleteAll();
		avatarDAO.deleteAll();
		userDao.deleteAll();
		heroDao.deleteAll();
	}

	private void initializeLogins() {
		for (ApplicationProperties.LoginData loginData : applicationProperties.getLogins()) {
			LoginInfoDO loginInfo = LoginInfoDO.builder()
					.username(loginData.getUsername())
					.password(loginData.getPassword())
					.build();
			loginInfoDao.save(loginInfo);
		}
	}

	private void initializeUsers() {
		for (ApplicationProperties.UserData userData : applicationProperties.getUsers()) {
			UserDO user = new UserDO();
			user.setUsername(userData.getUsername());
			user.setLastName(userData.getLastName());
			user.setFirstName(userData.getFirstName());
			user.setPreferredTheme(userData.getPreferredTheme());
			user = userDao.save(user);

			if (userData.getPrivileges() != null) {
				for (String privilege : userData.getPrivileges()) {
					PrivilegeDO privilegeDO = PrivilegeDO.builder()
							.userId(user.getUserId())
							.privilege(privilege)
							.build();
					user.getPrivileges().add(privilegeDO);
				}
				user = userDao.save(user);
			}

			if (userData.getAvatarFile() != null) {
				try (InputStream inputStream = this.getClass()
						.getClassLoader()
						.getResourceAsStream(userData.getAvatarFile())) {
					if (inputStream != null) {
						AvatarDO avatarDO = AvatarDO.builder()
								.userId(user.getUserId())
								.avatar(IOUtils.toByteArray(inputStream))
								.mimeType(userData.getAvatarMimeType())
								.build();
						avatarDAO.save(avatarDO);
					}
				} catch (IOException e) {
					log.error("Unable to load avatar file " + userData.getAvatarFile(), e);
				}
			}
		}
	}

	private void initializeHeroes() {
		for (ApplicationProperties.HeroData heroData : applicationProperties.getHeroes()) {
			HeroDO hero = HeroDO.builder()
					.name(heroData.getName())
					.alterEgo(heroData.getAlterEgo())
					.power(heroData.getPower())
					.rating(heroData.getRating())
					.build();
			heroDao.save(hero);
		}
	}
}