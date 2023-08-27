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
import java.util.Map.Entry;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterMatchMode;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterData;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterOperator;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;

/**
 * Specification class to process the UIFilter object an produce a JPA predicate.
 */
@AllArgsConstructor
public class ProcessFilter implements Specification<HeroDO> {

	@Serial
	private static final long serialVersionUID = 1L;

	// Map transfer object fields to domain object fields.
	// There is no good reason these should ever vary, but let's be paranoid.
	private final Map<String, String> nameMap = new HashMap<>() {
		@Serial
		private static final long serialVersionUID = 1L;

		{
			put("name", "name");
			put("power", "power");
			put("alterEgo", "alterEgo");
		}
	};

	private final UIFilter filter;

	// This implementation is very incomplete, but does match String values using contains.
	@Override
	public Predicate toPredicate(Root<HeroDO> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> outer = new ArrayList<>();
		if (filter.getFilters() != null && !filter.getFilters().isEmpty()) {
			for (Entry<String, List<UIFilterData>> entry : filter.getFilters().entrySet()) {
				String key;
				if ("global".equals(entry.getKey())) {
					key = "name";
				} else {
					key = nameMap.get(entry.getKey());
				}

				if (key != null) {
					List<Predicate> inner = new ArrayList<>();
					UIFilterOperator operator = null;
					for (UIFilterData md : entry.getValue()) {
						if (md.getOperator() != null) {
							operator = md.getOperator();
						}

						inner.add(getPredicate(root, cb, md, key, md.getValue()));
					}

					if (!inner.isEmpty()) {
						if (operator == null || operator == UIFilterOperator.or) {
							outer.add(cb.or(inner.toArray(new Predicate[inner.size()])));
						} else {
							outer.add(cb.and(inner.toArray(new Predicate[inner.size()])));
						}
					}
				}
			}

			if (!outer.isEmpty()) {
				return cb.and(outer.toArray(new Predicate[outer.size()]));
			}
		}
		return null;
	}

	private static Predicate getPredicate(Root<HeroDO> root, CriteriaBuilder cb, UIFilterData md, String key, Object value) {
		return switch (getMatchMode(md)) {
			// Needs date conversion
			case after -> cb.greaterThan(root.get(key), value.toString());
			// Needs date conversion
			case before -> cb.lessThan(root.get(key), value.toString());
			// Bogus -- where do I get both operands?
			case between -> cb.equal(root.get(key), value.toString());
			case contains -> cb.like(cb.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%");
			// Needs date conversion
			case dateAfter -> cb.greaterThan(root.get(key), value.toString());
			// Needs date conversion
			case dateBefore -> cb.lessThan(root.get(key), value.toString());
			// Needs date conversion
			case dateIs -> cb.equal(root.get(key), value.toString());
			// Needs date conversion
			case dateIsNot -> cb.notEqual(root.get(key), value.toString());
			case endsWith -> cb.like(cb.lower(root.get(key)), "%" + value.toString().toLowerCase());
			case equals -> cb.equal(cb.lower(root.get(key)), value.toString().toLowerCase());
			case gt -> cb.greaterThan(cb.lower(root.get(key)), value.toString().toLowerCase());
			case gte -> cb.greaterThanOrEqualTo(cb.lower(root.get(key)), value.toString().toLowerCase());
			// Bogus -- needs to be a list
			case in -> cb.equal(cb.lower(root.get(key)), value.toString().toLowerCase());
			case is -> cb.equal(cb.lower(root.get(key)), value.toString().toLowerCase());
			case isNot -> cb.notEqual(cb.lower(root.get(key)), value.toString().toLowerCase());
			case lt -> cb.lessThan(cb.lower(root.get(key)), value.toString().toLowerCase());
			case lte -> cb.lessThanOrEqualTo(cb.lower(root.get(key)), value.toString().toLowerCase());
			case notContains -> cb.notLike(cb.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%");
			case notEquals -> cb.notEqual(cb.lower(root.get(key)), value.toString().toLowerCase());
			case startsWith -> cb.like(cb.lower(root.get(key)), value.toString().toLowerCase() + "%");
			default -> throw new RuntimeException("Unknown matchmode: " + md.getMatchMode());
		};
	}

	private static UIFilterMatchMode getMatchMode(UIFilterData md) {
		// TODO Ought to check data type and set operator.
		return md.getMatchMode() == null ? UIFilterMatchMode.contains : md.getMatchMode();
	}
}