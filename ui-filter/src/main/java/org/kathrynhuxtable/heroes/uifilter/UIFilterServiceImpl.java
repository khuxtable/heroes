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
package org.kathrynhuxtable.heroes.uifilter;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.uifilter.bean.UIFilter;
import org.kathrynhuxtable.heroes.uifilter.bean.UIFilter.UIFilterSort;

@Component
public class UIFilterServiceImpl<T> implements UIFilterService<T> {

	@Override
	public long countByFilter(@NonNull UIFilter filter,
	                          @NonNull DescriptorMap descriptorMap,
	                          @NonNull JpaSpecificationExecutor<T> dao) {
		return dao.count(new FilterSpecification<>(filter, descriptorMap));
	}

	@Override
	public List<T> findByFilter(@NonNull UIFilter filter,
	                            String defaultField,
	                            @NonNull DescriptorMap descriptorMap,
	                            @NonNull JpaSpecificationExecutor<T> dao) {
		// Create JPA sort criteria.
		Sort sort = buildSort(filter, defaultField, descriptorMap);

		// Create the filter predicate.
		FilterSpecification<T> filterSpecification = new FilterSpecification<>(filter, descriptorMap);

		// Find the rows, paginating if requested.
		if (filter.getRows() == null || filter.getRows() == 0) {
			return dao.findAll(filterSpecification, sort);
		} else {
			int rows = filter.getRows();
			int first = filter.getFirst() == null ? 0 : filter.getFirst();
			int page = first / rows;
			Page<T> pageable = dao.findAll(filterSpecification, PageRequest.of(page, rows, sort));
			return pageable.getContent();
		}
	}

	@Override
	public Sort buildSort(@NonNull UIFilter filter,
	                      String defaultField,
	                      @NonNull DescriptorMap descriptorMap) {
		// Create JPA sort criteria. Sort on id by default.
		if (filter.getSortFields() == null || filter.getSortFields().isEmpty()) {
			if (defaultField != null && !defaultField.isBlank() && descriptorMap.get(defaultField) != null) {
				return Sort.by(Sort.Direction.ASC, descriptorMap.get(defaultField).attributeName);
			} else {
				return Sort.unsorted();
			}
		} else {
			List<Order> orders = new ArrayList<>();
			for (UIFilterSort sortField : filter.getSortFields()) {
				Direction direction = sortField.getOrder() > 0 ? Sort.Direction.ASC : Sort.Direction.DESC;
				if (descriptorMap.get(sortField.getField()) != null) {
					orders.add(new Order(direction, descriptorMap.get(sortField.getField()).attributeName));
				}
			}
			return Sort.by(orders);
		}
	}
}
