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

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;

import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import org.kathrynhuxtable.heroes.uifilter.bean.UIFilter;
import org.kathrynhuxtable.heroes.uifilter.bean.UIFilter.UIFilterData;
import org.kathrynhuxtable.heroes.uifilter.bean.UIFilter.UIFilterMatchMode;
import org.kathrynhuxtable.heroes.uifilter.bean.UIFilter.UIFilterOperator;
import org.kathrynhuxtable.heroes.uifilter.UIFilterService.DescriptorMap;
import org.kathrynhuxtable.heroes.uifilter.UIFilterService.FieldDescriptor;


/**
 * Specification class to process the UIFilter object an produce a JPA predicate.
 */
@Slf4j
public class FilterSpecification<T> implements Specification<T> {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * The filter for which to generate a predicate.
	 */
	private final UIFilter filter;

	/**
	 * Map transfer object fields to domain object fields, along with their data type,
	 * and whether they are included in global searches.
	 */
	private final DescriptorMap descriptorMap;

	/**
	 * Construct a FilterSpecification, which constructs a JPA Predicate matching a UIFilter.
	 *
	 * @param filter        the UIFilter from the UI.
	 * @param descriptorMap the DescriptorMap, mapping the filter fields to domain objects and types.
	 */
	public FilterSpecification(@NonNull UIFilter filter, @NonNull DescriptorMap descriptorMap) {
		this.filter = filter;
		this.descriptorMap = descriptorMap;
	}

	@Override
	public Predicate toPredicate(@NonNull Root<T> root, @NonNull CriteriaQuery<?> cq, @NonNull CriteriaBuilder cb) {
		if (filter.getFilters() == null) {
			return null;
		}

		List<Predicate> outer = new ArrayList<>();
		for (Entry<String, List<UIFilterData>> entry : filter.getFilters().entrySet()) {
			Predicate inner = buildFieldPredicate(root, cb, entry);
			if (inner != null) {
				outer.add(inner);
			}
		}

		if (outer.isEmpty()) {
			return null;
		} else {
			return cb.and(outer.toArray(new Predicate[0]));
		}
	}

	private Predicate buildFieldPredicate(Root<T> root, CriteriaBuilder cb, Entry<String, List<UIFilterData>> entry) {
		List<Predicate> inner = new ArrayList<>();
		UIFilterOperator operator = null;

		for (UIFilterData filterData : entry.getValue()) {
			if (filterData.getOperator() != null) {
				operator = filterData.getOperator();
			}

			if ("global".equals(entry.getKey())) {
				List<Predicate> globals = descriptorMap.values().stream()
						.filter(fd -> fd.global)
						.map(fd -> getPredicate(root, cb, descriptorMap.get(fd.attributeName), filterData))
						.toList();
				inner.add(cb.or(globals.toArray(new Predicate[0])));
			} else {
				inner.add(getPredicate(root, cb, descriptorMap.get(entry.getKey()), filterData));
			}
		}

		if (inner.isEmpty()) {
			return null;
		} else if (operator == null || operator == UIFilterOperator.or) {
			return cb.or(inner.toArray(new Predicate[0]));
		} else if (operator == UIFilterOperator.and) {
			return cb.and(inner.toArray(new Predicate[0]));
		} else {
			return null;
		}
	}

	private Predicate getPredicate(Root<T> root, CriteriaBuilder cb,
	                               FieldDescriptor fieldDescriptor, UIFilterData filterData) {
		return switch (fieldDescriptor.dataType) {
			case text -> getPredicate(
					cb,
					getMatchMode(filterData, fieldDescriptor),
					cb.lower(root.get(fieldDescriptor.attributeName)),
					((String) filterData.getValue()).toLowerCase());
			case numeric -> filterData.getValue() instanceof Integer ?
					getPredicate(
							cb,
							getMatchMode(filterData, fieldDescriptor),
							root.get(fieldDescriptor.attributeName),
							(Integer) filterData.getValue()) :
					getPredicate(
							cb,
							getMatchMode(filterData, fieldDescriptor),
							root.get(fieldDescriptor.attributeName),
							(Double) filterData.getValue());
			case date -> getPredicate(
					cb,
					getMatchMode(filterData, fieldDescriptor),
					root.get(fieldDescriptor.attributeName),
					Date.from(Instant.parse((String) filterData.getValue())));
		};
	}

	@SuppressWarnings("unchecked")
	private static <FT extends Comparable<FT>> Predicate getPredicate(CriteriaBuilder cb, UIFilterMatchMode matchMode,
	                                                                  Expression<FT> fieldExpression, FT value) {
		// The in and between match modes are not yet implemented.
		// The PrimeNG table lazy loader doesn't seem to generate them.
		return switch (matchMode) {
			case contains -> cb.like((Expression<String>) fieldExpression, "%" + value + "%");
			case notContains -> cb.notLike((Expression<String>) fieldExpression, "%" + value + "%");
			case startsWith -> cb.like((Expression<String>) fieldExpression, value + "%");
			case endsWith -> cb.like((Expression<String>) fieldExpression, "%" + value);
			case equals -> cb.equal(fieldExpression, value);
			case gt -> cb.greaterThan(fieldExpression, value);
			case gte -> cb.greaterThanOrEqualTo(fieldExpression, value);
			case lt -> cb.lessThan(fieldExpression, value);
			case lte -> cb.lessThanOrEqualTo(fieldExpression, value);
			case notEquals -> cb.notEqual(fieldExpression, value);
			default -> throw new RuntimeException("Invalid matchmode: " + matchMode);
		};
	}

	private static UIFilterMatchMode getMatchMode(UIFilterData filterData, FieldDescriptor fieldDescriptor) {
		UIFilterMatchMode matchMode = filterData.getMatchMode();
		if (matchMode == null) {
			matchMode = switch (fieldDescriptor.dataType) {
				case text -> UIFilterMatchMode.contains;
				case numeric, date -> UIFilterMatchMode.equals;
			};
		}
		return matchMode;
	}
}