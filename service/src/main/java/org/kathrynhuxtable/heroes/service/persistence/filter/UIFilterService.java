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

import org.kathrynhuxtable.heroes.service.bean.UIFilter;

/**
 * Provide common methods for the UIFilterDescriptor annotation.
 * <br/>
 * This specifies how filter fields are mapped to domain fields.
 */
public interface UIFilterService<T> {

	/**
	 * Represents the three data types that need different handling in filters.
	 */
	enum DataType {
		text, numeric, date;
	}

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
	 * Map to map filter fields from the UI into JPA fields, along with their filter data type,
	 * and whether the field is included in a global search.
	 */
	class DescriptorMap extends HashMap<String, FieldDescriptor> {
	}

	/**
	 * Find by filter. Supports pagination, sorting, and filtering on values.
	 *
	 * @param filter        the UIFilter object.
	 * @param defaultField  optional default field to sort by.
	 * @param descriptorMap descriptor map
	 * @param dao           the associated DAO object.
	 * @return a List of matching domain records.
	 */
	List<T> findByFilterPaginated(UIFilter filter,
	                              String defaultField,
	                              DescriptorMap descriptorMap,
	                              JpaSpecificationExecutor<T> dao);

	/**
	 * Build a JPA sort.
	 *
	 * @param filter        the UIFilter object.
	 * @param defaultField  optional default field to sort by.
	 * @param descriptorMap descriptor map
	 * @return a Sort object representing the requested sort order.
	 */
	Sort buildSort(UIFilter filter,
	               String defaultField,
	               DescriptorMap descriptorMap);
}
