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

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;
import org.kathrynhuxtable.heroes.service.persistence.filter.UIFilterService;

/**
 * The Hero DAO. Provides some convenience methods using custom predicates generated
 * by Specification classes.
 */
@Repository
public interface HeroDAO extends JpaRepository<HeroDO, Long>, JpaSpecificationExecutor<HeroDO>, UIFilterService<HeroDO> {

	// Map UI field to JPA field properties.
	DescriptorMap descriptorMap = new DescriptorMap() {{
		put("name", FieldDescriptor.builder().attributeName("name").dataType(DataType.text).global(true).build());
		put("power", FieldDescriptor.builder().attributeName("power").dataType(DataType.text).global(true).build());
		put("alterEgo", FieldDescriptor.builder().attributeName("alterEgo").dataType(DataType.text).global(true).build());
		put("rating", FieldDescriptor.builder().attributeName("rating").dataType(DataType.numeric).build());
		put("powerDate", FieldDescriptor.builder().attributeName("powerDate").dataType(DataType.date).build());
	}};

	/**
	 * Return the number of rows matched by filter criteria without paginating.
	 * This is needed for a UI to know how many pages are available.
	 *
	 * @param filter the UIFilter object.
	 * @return the number of rows matched by the filter criteria.
	 */
	default long countByFilter(UIFilter filter) {
		return countByFilter(filter, descriptorMap, this);
	}

	/**
	 * Find by filter. Supports pagination, sorting, and filtering on values.
	 *
	 * @param filter the UIFilter object.
	 * @return a List of matching HeroDO records.
	 */
	default List<HeroDO> findByFilter(UIFilter filter) {
		return findByFilter(filter, "id", descriptorMap, this);
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
}
