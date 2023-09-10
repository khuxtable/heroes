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

import java.util.ArrayList;
import java.util.List;

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
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;
import org.kathrynhuxtable.heroes.service.persistence.filter.ProcessFilter;
import org.kathrynhuxtable.heroes.service.persistence.filter.UIFilterService;

/**
 * The Hero DAO. Provides some convenience methods using custom predicates generated
 * by Specification classes.
 */
@Repository
public interface HeroDAO extends JpaRepository<HeroDO, Long>, JpaSpecificationExecutor<HeroDO>, UIFilterService {

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
		ProcessFilter<HeroDO> process = new ProcessFilter<>(getDescriptorMap(HeroDO.class), filter);

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
		return count(new ProcessFilter<>(getDescriptorMap(HeroDO.class), filter));
	}
}
