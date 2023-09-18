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

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterSort;

@Component
public class UIFilterServiceImpl<T> implements UIFilterService<T> {

	@Override
	public List<T> findByFilterPaginated(UIFilter filter,
	                                     String defaultField,
	                                     DescriptorMap descriptorMap,
	                                     JpaSpecificationExecutor<T> dao) {
		// Create JPA sort criteria.
		Sort sort = buildSort(filter, defaultField, descriptorMap);

		// Create the filter predicate.
		ProcessFilter<T> process = new ProcessFilter<>(descriptorMap, filter);

		// Find the rows, paginating if requested.
		if (filter.getRows() == null || filter.getRows() == 0) {
			return dao.findAll(process, sort);
		} else {
			int rows = filter.getRows();
			int first = filter.getFirst() == null ? 0 : filter.getFirst();
			int page = first / rows;
			Page<T> pageable = dao.findAll(process, PageRequest.of(page, rows, sort));
			return pageable.getContent();
		}
	}

	@Override
	public Sort buildSort(UIFilter filter,
	                      String defaultField,
	                      DescriptorMap descriptorMap) {
		// Create JPA sort criteria. Sort on id by default.
		if (filter.getSortFields() == null || filter.getSortFields().isEmpty()) {
			if (defaultField != null && !defaultField.isBlank() && descriptorMap.get(defaultField) != null) {
				return Sort.by(Sort.Direction.ASC, descriptorMap.get(defaultField).fieldName);
			} else {
				return Sort.unsorted();
			}
		} else {
			List<Order> orders = new ArrayList<>();
			for (UIFilterSort sortField : filter.getSortFields()) {
				Direction direction = sortField.getOrder() > 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
				if (descriptorMap.get(sortField.getField()) != null) {
					orders.add(new Order(direction, descriptorMap.get(sortField.getField()).fieldName));
				}
			}
			return Sort.by(orders);
		}
	}
}
