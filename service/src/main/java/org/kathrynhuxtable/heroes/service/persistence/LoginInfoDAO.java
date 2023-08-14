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
package org.kathrynhuxtable.heroes.service.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.kathrynhuxtable.heroes.service.persistence.domain.LoginInfoDO;

/**
 * The LoginInfo DAO.
 */
@Repository
public interface LoginInfoDAO extends JpaRepository<LoginInfoDO, Long>, JpaSpecificationExecutor<LoginInfoDO> {

	/**
	 * Find the matching authentication credentials.
	 *
	 * @param username the username to match.
	 * @param password the password to match.
	 * @return the LoginInfoDO, or {@9ode null} if the username and psasword do not match a record.
	 */
	LoginInfoDO findLoginInfoDOByUsernameAndPassword(String username, String password);
}
