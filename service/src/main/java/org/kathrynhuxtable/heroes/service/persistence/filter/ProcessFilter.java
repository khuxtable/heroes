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

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import jakarta.persistence.criteria.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.NonNull;

import org.kathrynhuxtable.heroes.service.persistence.filter.FieldDescriptor;
import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterData;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterMatchMode;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterOperator;

/**
 * Specification class to process the UIFilter object an produce a JPA predicate.
 */
@Slf4j
@AllArgsConstructor
public class ProcessFilter<T> implements Specification<T> {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Map transfer object fields to domain object fields.
	 * There is no good reason these should ever vary, but let's be paranoid.
	 */
	private final Map<String, FieldDescriptor> nameMap;

	/**
	 * The filter for which to generate a predicate.
	 */
	private final UIFilter filter;

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

		for (UIFilterData md : entry.getValue()) {
			if (md.getOperator() != null) {
				operator = md.getOperator();
			}

			if ("global".equals(entry.getKey())) {
				List<Predicate> globals = nameMap.values().stream()
						.filter(nm -> nm.global)
						.map(nm -> getPredicate(root, cb, nameMap.get(nm.fieldName), md))
						.toList();
				inner.add(cb.or(globals.toArray(new Predicate[0])));
			} else {
				inner.add(getPredicate(root, cb, nameMap.get(entry.getKey()), md));
			}
		}

		if (inner.isEmpty()) {
			return null;
		} else if (operator == null || operator == UIFilterOperator.or) {
			return cb.or(inner.toArray(new Predicate[0]));
		} else {
			return cb.and(inner.toArray(new Predicate[0]));
		}
	}

	private Predicate getPredicate(Root<T> root, CriteriaBuilder cb,
	                               FieldDescriptor fieldDescriptor, UIFilterData md) {
		return switch (fieldDescriptor.dataType) {
			case text -> getPredicate(
					cb,
					getMatchMode(md, fieldDescriptor),
					cb.lower(root.get(fieldDescriptor.fieldName)),
					((String) md.getValue()).toLowerCase());
			case numeric -> md.getValue() instanceof Integer ?
					getPredicate(
							cb,
							getMatchMode(md, fieldDescriptor),
							root.get(fieldDescriptor.fieldName),
							(Integer) md.getValue()) :
					getPredicate(
							cb,
							getMatchMode(md, fieldDescriptor),
							root.get(fieldDescriptor.fieldName),
							(Double) md.getValue());
			case date -> getPredicate(
					cb,
					getMatchMode(md, fieldDescriptor),
					root.get(fieldDescriptor.fieldName),
					Date.from(Instant.parse((String) md.getValue())));
		};
	}

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

	private static UIFilterMatchMode getMatchMode(UIFilterData md, FieldDescriptor fieldDescriptor) {
		UIFilterMatchMode matchMode = md.getMatchMode();
		if (matchMode == null) {
			matchMode = switch (fieldDescriptor.dataType) {
				case text -> UIFilterMatchMode.contains;
				case numeric, date -> UIFilterMatchMode.equals;
			};
		}
		return matchMode;
	}
}