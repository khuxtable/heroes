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
import java.util.Map;

import lombok.Data;

/**
 * A somewhat simplified filter data structure, holding the pagination information,
 * the sort information, and filtering criteria.
 */
@Data
public class UIFilter {

	/**
	 * The index of the first record to be loaded.
	 */
	Integer first;

	/**
	 * The number of rows to load.
	 */
	Integer rows;

	/**
	 * The fields and orders to be used for sorting.
	 */
	List<UIFilterSort> sortFields;

	/**
	 * An object containing filter metadata for filtering the data. The keys
	 * represent the field names, and the values represent the corresponding filter
	 * metadata.
	 */
	Map<String, List<UIFilterData>> filters;

	@Data
	public static class UIFilterSort {
		String field;
		Integer order;
	}

	@Data
	public static class UIFilterData {
		/**
		 * The value used for filtering.
		 */
		Object value;

		/**
		 * The match mode for filtering.
		 */
		UIFilterMatchMode matchMode;

		/**
		 * The operator for filtering.
		 */
		UIFilterOperator operator;
	}

	public enum UIFilterMatchMode {
		startsWith, contains, notContains, endsWith, equals, notEquals, in, lt, lte, gt, gte, between
	}

	public enum UIFilterOperator {
		and, or
	}
}
