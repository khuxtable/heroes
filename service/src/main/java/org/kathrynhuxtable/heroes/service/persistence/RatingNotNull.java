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

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;

/**
 * Specification class to match non-null rating values in UserDO objects.
 */
public class RatingNotNull implements Specification<HeroDO> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Override
    public Predicate toPredicate(Root<HeroDO> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
        return cb.isNotNull(root.get("rating"));
    }
}