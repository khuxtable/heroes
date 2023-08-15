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
package org.kathrynhuxtable.heroes.service.bean;

import java.util.List;

import lombok.Builder;
import lombok.Data;

/**
 * The User transfer object for the REST services.
 */
@Data
@Builder
public class User {

	private long id;
	private String username;
	private String firstName;
	private String lastName;
	private String preferredTheme;

	private List<String> privileges;
}
