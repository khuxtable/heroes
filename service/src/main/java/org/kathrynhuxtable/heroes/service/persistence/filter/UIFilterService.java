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

import java.util.HashMap;
import java.util.List;

import lombok.Builder;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;

import org.kathrynhuxtable.heroes.service.bean.UIFilter;

/**
 * Provide common methods for the UIFilterDescriptor annotation.
 * <p>
 * Defines the DescriptorMap, specifying how filter fields are mapped to domain fields.
 * </p>
 */
public interface UIFilterService<T> {

	/**
	 * Represents the three data types that need different handling in filters.
	 * <dl>
	 * <dt>text</dt>
	 * <dd>Used for String values. Default matchMode is "contains".</dd>
	 * <dt>numeric</dt>
	 * <dd>Used for Integer and Double values. Default matchMode is "equals".</dd>
	 * <dt>date</dt>
	 * <dd>Used for Date, and Calendar values. Default matchMode is "equals".</dd>
	 * </dl>
	 */
	enum DataType {
		text, numeric, date
	}

	/**
	 * Contains the domain object attribute name, the filter data type,
	 * and whether this field should be included in global searches.
	 */
	@Builder
	class FieldDescriptor {
		/**
		 * The domain object attribute name.
		 */
		public final String attributeName;

		/**
		 * The filter data type, used for constructing predicates.
		 */
		public final DataType dataType;

		/**
		 * {@code true} if this field should be included in global searches.
		 */
		public final boolean global;
	}

	/**
	 * Map to map filter fields from the UI into JPA fields, along with their filter data type,
	 * and whether the field is included in a global search.
	 */
	class DescriptorMap extends HashMap<String, FieldDescriptor> {
	}

	/**
	 * Return the number of rows matched by filter criteria without paginating.
	 * This is needed for a UI to know how many pages are available.
	 *
	 * @param filter        the UIFilter object.
	 * @param descriptorMap descriptor map.
	 * @param dao           the associated DAO object.
	 * @return the number of rows matched by the filter criteria.
	 */
	long countByFilter(@NonNull UIFilter filter,
	                   @NonNull DescriptorMap descriptorMap,
	                   @NonNull JpaSpecificationExecutor<T> dao);

	/**
	 * Find by filter. Supports pagination, sorting, and filtering on values.
	 *
	 * @param filter        the UIFilter object.
	 * @param defaultField  optional default field to sort by.
	 * @param descriptorMap descriptor map.
	 * @param dao           the associated DAO object.
	 * @return a List of matching domain records.
	 */
	List<T> findByFilter(@NonNull UIFilter filter,
	                     String defaultField,
	                     @NonNull DescriptorMap descriptorMap,
	                     @NonNull JpaSpecificationExecutor<T> dao);

	/**
	 * Build a JPA sort.
	 *
	 * @param filter        the UIFilter object.
	 * @param defaultField  optional default field to sort by.
	 * @param descriptorMap descriptor map
	 * @return a Sort object representing the requested sort order.
	 */
	Sort buildSort(@NonNull UIFilter filter,
	               String defaultField,
	               @NonNull DescriptorMap descriptorMap);
}
