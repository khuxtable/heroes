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
package org.kathrynhuxtable.heroes.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.Hero;
import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterData;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterMatchMode;
import org.kathrynhuxtable.heroes.service.bean.UIFilterResult;
import org.kathrynhuxtable.heroes.service.persistence.HeroDAO;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;

/**
 * The Hero service. Provides an API for searching, saving, updating, and deleting Hero objects.
 */
@Slf4j
@Component
@AllArgsConstructor
public class HeroService {

	private final HeroDAO heroDao;

	/**
	 * Search by filter.
	 *
	 * @param filter the UIFilter from the client.
	 * @return the HeroFilterResult.
	 */
	public UIFilterResult<Hero> find(UIFilter filter) {
		log.info("Filter: " + filter.toString());
		long totalRecords = heroDao.countByFilter(filter);
		List<HeroDO> heroes = heroDao.findByFilter(filter);

		UIFilterResult<Hero> result = new UIFilterResult<>();
		result.setRecords(toHeroes(heroes));
		result.setTotalRecords((int) totalRecords);
		return result;
	}

	/**
	 * Find the first five heroes with the top rating values.
	 * @return a List of up to 5 Hero objects.
	 */
	public List<Hero> findTopHeroes() {
		return toHeroes(heroDao.findTopHeroes(5));
	}

	/**
	 * A simple "search by name" filter. Returns all matches containing the name.
	 *
	 * @param name the name text to be matched. Ignores case.
	 * @return a List of Hero objects matching the name.
	 */
	public List<Hero> search(String name) {
		UIFilter filter = new UIFilter();
		UIFilterData md = new UIFilterData();
		md.setValue(name);
		md.setMatchMode(UIFilterMatchMode.contains);
		Map<String, List<UIFilterData>> filterMap = new HashMap<>();
		filterMap.put("name", Collections.singletonList(md));
		filter.setFilters(filterMap);
		return toHeroes(heroDao.findByFilter(filter));
	}

	/**
	 * Find by ID.
	 *
	 * @param id the Hero id value.
	 * @return the Hero matching the id, or {@code null} if no match.
	 */
	public Hero find(long id) {
		log.info("Searching for hero " + id);
		Optional<HeroDO> heroDO = heroDao.findById(id);
		return heroDO.map(this::toHero).orElse(null);
	}

	/**
	 * Save or update a Hero. If the id is null or zero, a new Hero will be created,
	 * otherwise an update will be attempted. Same as {@code save} method.
	 *
	 * @param hero the Hero data to save or update.
	 * @return the saved Hero, or {@code null} if no match on id.
	 */
	public Hero update(Hero hero) {
		return toHero(heroDao.save(toHeroDO(hero)));
	}

	/**
	 * Save or update a Hero. If the id is null or zero, a new Hero will be created,
	 * otherwise an update will be attempted. Same as {@code update} method.
	 *
	 * @param hero the Hero data to save or update.
	 * @return the saved Hero, or {@code null} if no match on id.
	 */
	public Hero save(Hero hero) {
		return toHero(heroDao.save(toHeroDO(hero)));
	}

	/**
	 * Delete a Hero object.
	 *
	 * @param id the id value to delete.
	 * @return the Hero that was deleted.
	 */
	public Hero delete(long id) {
		Optional<HeroDO> found = heroDao.findById(id);
        found.ifPresent(heroDao::delete);
		return found.map(this::toHero).orElse(null);
	}

	/**
	 * Convert List of domain objects to List of transfer objects.
	 *
	 * @param heroDOs a List of HeroDO objects.
	 * @return a List of Hero objects.
	 */
	private List<Hero> toHeroes(List<HeroDO> heroDOs) {
		return heroDOs.stream().map(this::toHero).collect(Collectors.toList());
	}

	/**
	 * Convert domain object to List of transfer object.
	 *
	 * @param heroDO a HeroDO object.
	 * @return a Hero object.
	 */
	private Hero toHero(HeroDO heroDO) {
		if (heroDO == null) {
			return null;
		} else {
			return Hero.builder()
					.id(heroDO.getId())
					.name(heroDO.getName())
					.power(heroDO.getPower())
					.alterEgo(heroDO.getAlterEgo())
					.rating(heroDO.getRating())
					.powerDate(heroDO.getPowerDate())
					.build();
		}
	}

	/**
	 * Convert a transfer object to a domain object.
	 *
	 * @param hero a Hero object.
	 * @return a HeroDO object.
	 */
	private HeroDO toHeroDO(Hero hero) {
		if (hero == null) {
			return null;
		} else {
			HeroDO heroDO = new HeroDO();
			heroDO.setId(hero.getId());
			heroDO.setName(hero.getName());
			heroDO.setPower(hero.getPower());
			heroDO.setAlterEgo(hero.getAlterEgo());
			heroDO.setRating(hero.getRating());
			heroDO.setPowerDate(hero.getPowerDate());
			return heroDO;
		}
	}
}
