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

import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterSort;
import org.kathrynhuxtable.heroes.service.persistence.ProcessFilter.DataType;
import org.kathrynhuxtable.heroes.service.persistence.ProcessFilter.FieldDescriptor;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;

/**
 * The Hero DAO. Provides some convenience methods using custom predicates generated
 * by Specification classes.
 */
@Repository
public interface HeroDAO extends JpaRepository<HeroDO, Long>, JpaSpecificationExecutor<HeroDO> {

	// Map transfer object fields to domain object fields/types.
	Map<String, FieldDescriptor> nameMap = new HashMap<>() {
		@Serial
		private static final long serialVersionUID = 1L;

		{
			put("name", new FieldDescriptor("name", DataType.text, true));
			put("power", new FieldDescriptor("power", DataType.text, true));
			put("alterEgo", new FieldDescriptor("alterEgo", DataType.text, true));
			put("rating", new FieldDescriptor("rating", DataType.numeric, false));
			put("powerDate", new FieldDescriptor("powerDate", DataType.date, false));
		}
	};

	/**
	 * Find by filter. Supports pagination, sorting, and filtering on values.
	 *
	 * @param filter the UIFilter object.
	 * @return a List of matching HeroDO records.
	 */
	default List<HeroDO> findByFilter(UIFilter filter) {
		// Create JPA sort criteria. Sort on id by default.
		Sort sort;
		if (filter.getSortFields() == null || filter.getSortFields().isEmpty()) {
			sort = Sort.by(Sort.Direction.ASC, "id");
		} else {
			List<Order> orders = new ArrayList<>();
			for (UIFilterSort sortField : filter.getSortFields()) {
				Direction direction = sortField.getOrder() > 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
				orders.add(new Order(direction, sortField.getField()));
			}
			sort = Sort.by(orders);
		}

		// Create the filter predicate.
		ProcessFilter process = new ProcessFilter(filter, nameMap);

		// Find the rows, paginating if requested.
		if (filter.getRows() == null || filter.getRows() == 0) {
			return findAll(process, sort);
		} else {
			int rows = filter.getRows();
			int first = filter.getFirst() == null ? 0 : filter.getFirst();
			int page = first / rows;
			Page<HeroDO> pageable = findAll(process, PageRequest.of(page, rows, sort));
			return pageable.getContent();
		}
	}

	/**
	 * Return the top rated Heroes.
	 *
	 * @param count the number of Heroes to return.
	 * @return a List of HeroDO objects
	 */
	default List<HeroDO> findTopHeroes(int count) {
		RatingNotNull goodRating = new RatingNotNull();
		Sort sort = Sort.by(Sort.Direction.DESC, "rating");

		Page<HeroDO> pageable = findAll(goodRating, PageRequest.of(0, count, sort));
		return pageable.getContent();
	}

	/**
	 * Return the number of rows matched by filter criteria without paginating.
	 * This is needed for a UI to know how many pages are available.
	 *
	 * @param filter the UIFilter object.
	 * @return the number of rows matched by the filter criteria.
	 */
	default long countByFilter(UIFilter filter) {
		ProcessFilter process = new ProcessFilter(filter, nameMap);
		return count(process);
	}
}
