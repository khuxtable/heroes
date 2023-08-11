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
package org.kathrynhuxtable.heroes.service.persistence;

import java.util.ArrayList;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterSort;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterMetadata;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;

@Component
public interface HeroDAO extends JpaRepository<HeroDO, Long>, JpaSpecificationExecutor<HeroDO> {

    default List<HeroDO> findByFilter(UIFilter filter) {
        ProcessFilter process = new ProcessFilter(filter);

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

    default List<HeroDO> findTopHeroes(int count) {
        RatingNotNull goodRating = new RatingNotNull();
        Sort sort= Sort.by(Sort.Direction.DESC, "rating");

        Page<HeroDO> pageable = findAll(goodRating, PageRequest.of(0, count, sort));
        return pageable.getContent();
    }

    default long countByFilter(UIFilter filter) {
        ProcessFilter process = new ProcessFilter(filter);
        return count(process);
    }

    @Transactional
    default void initHeroes() {
        for (HeroDO hero : findAll()) {
            delete(hero);
        }

        String[] initialHeroes = new String[]{"Dr. Nice", "Bombasto", "Celeritas", "Magneta", "RubberMan", "Dynama",
                "Dr. IQ", "Magma", "Tornado"};

        for (String name : initialHeroes) {
            HeroDO hero = new HeroDO();
            hero.setName(name);
            hero.setPower("");
            if ("Bombasto".equals(name)) {
                hero.setRating(4);
            } else if ("Tornado".equals(name)) {
                hero.setRating(3);
            } else if ("Celeritas".equals(name) || "Magneta".equals(name) || "RubberMan".equals(name) || "Dynama".equals(name)) {
                hero.setRating(5);
            } else {
                hero.setRating(0);
            }
            save(hero);
        }
    }
}
