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
package org.kathrynhuxtable.heroes.service.persistence.filter;

import java.time.temporal.Temporal;
import java.util.Calendar;
import java.util.Date;

public enum DataType {
	text, numeric, date;

	public static DataType getDataType(Class<?> fClass) {
		if (Number.class.isAssignableFrom(fClass)) {
			return numeric;
		} else if (Date.class.isAssignableFrom(fClass) || java.sql.Date.class.isAssignableFrom(fClass)
				|| Calendar.class.isAssignableFrom(fClass) || Temporal.class.isAssignableFrom(fClass)) {
			return date;
		} else {
			return text;
		}
	}
}
