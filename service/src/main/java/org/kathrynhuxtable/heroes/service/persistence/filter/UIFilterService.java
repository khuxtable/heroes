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
import java.util.Map;

import lombok.Builder;

/**
 * Provide annotation processing and retrieval for the UIFilterDescriptor annotation.
 * <br/>
 * This specifies how filter fields are mapped to domain fields.
 */
public interface UIFilterService {

	/**
	 * Retrieve the descriptor map for a class, processing its annotations if necessary.
	 *
	 * @param clazz the domain class to retrieve.
	 * @return a Map mapping the filter field names to their FieldDescriptor objects.
	 */
	Map<String, FieldDescriptor> getDescriptorMap(Class<?> clazz);

	/**
	 * Contains the domain field name, the filter data type,
	 * and whether this field should be included in global searches.
	 */
	@Builder
	class FieldDescriptor {
		public final String fieldName;
		public final DataType dataType;
		public final boolean global;
	}

	/**
	 * Represents the three data types that need different handling in filters.
	 */
	enum DataType {
		text, numeric, date;

		/**
		 * Given a Java class, return the DataTYpe.
		 *
		 * @param clazz the class of a domain field.
		 * @return the DataType corresponding to the class.
		 */
		public static DataType getDataType(Class<?> clazz) {
			if (Number.class.isAssignableFrom(clazz)) {
				return numeric;
			} else if (Date.class.isAssignableFrom(clazz) || java.sql.Date.class.isAssignableFrom(clazz)
					|| Calendar.class.isAssignableFrom(clazz) || Temporal.class.isAssignableFrom(clazz)) {
				return date;
			} else {
				return text;
			}
		}
	}
}
