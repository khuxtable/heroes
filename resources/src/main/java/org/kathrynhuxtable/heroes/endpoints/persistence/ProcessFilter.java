/*
 * Copyright 2002-2018 the original author or authors.
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
package org.kathrynhuxtable.heroes.endpoints.persistence;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.kathrynhuxtable.heroes.endpoints.bean.UIFilter;
import org.kathrynhuxtable.heroes.endpoints.bean.UIFilter.UIFilterMatchMode;
import org.kathrynhuxtable.heroes.endpoints.bean.UIFilter.UIFilterMetadata;
import org.kathrynhuxtable.heroes.endpoints.bean.UIFilter.UIFilterOperator;
import org.kathrynhuxtable.heroes.endpoints.persistence.domain.HeroDO;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class ProcessFilter implements Specification<HeroDO> {

	private static final long serialVersionUID = 1L;

	private Map<String, String> nameMap = new HashMap<>() {
		private static final long serialVersionUID = 1L;

		{
			put("name", "name");
			put("power", "power");
			put("alterEgo", "alterEgo");
		}
	};

	private UIFilter filter;

	ProcessFilter(UIFilter filter) {
		this.filter = filter;
	}

	@Override
	public Predicate toPredicate(Root<HeroDO> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> outer = new ArrayList<>();
		if (filter.getFilters() != null && !filter.getFilters().isEmpty()) {
			for (Entry<String, List<UIFilterMetadata>> entry : filter.getFilters().entrySet()) {
				String key;
				if ("global".equals(entry.getKey())) {
					key = "name";
				} else {
					key = nameMap.get(entry.getKey());
				}

				if (key != null) {
					List<Predicate> inner = new ArrayList<>();
					UIFilterOperator operator = null;
					for (UIFilterMetadata md : entry.getValue()) {
						if (md.getOperator() != null) {
							operator = md.getOperator();
						}

						UIFilterMatchMode matchMode = md.getMatchMode();
						if (matchMode == null) {
							// TODO Ought to check data type and set operator.
							matchMode = UIFilterMatchMode.contains;
						}

						Object value = md.getValue();

						switch (md.getMatchMode()) {
						case after:
							break;
						case before:
							break;
						case between:
							break;
						case contains:
							inner.add(cb.like(cb.lower(root.get(key)), "%" + value.toString().toLowerCase() + "%"));
							break;
						case dateAfter:
							break;
						case dateBefore:
							break;
						case dateIs:
							break;
						case dateIsNot:
							break;
						case endsWith:
							inner.add(cb.like(cb.lower(root.get(key)), "%" + value.toString().toLowerCase()));
							break;
						case equals:
							inner.add(cb.equal(cb.lower(root.get(key)), value.toString().toLowerCase()));
							break;
						case gt:
							break;
						case gte:
							break;
						case in:
							break;
						case is:
							break;
						case isNot:
							break;
						case lt:
							break;
						case lte:
							break;
						case notContains:
							break;
						case notEquals:
							inner.add(cb.notEqual(cb.lower(root.get(key)), value.toString().toLowerCase()));
							break;
						case startsWith:
							inner.add(cb.like(cb.lower(root.get(key)), value.toString().toLowerCase() + "%"));
							break;
						default:
							break;
						}
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
}