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
package org.kathrynhuxtable.heroes.service;

import java.util.*;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.kathrynhuxtable.heroes.service.bean.Hero;
import org.kathrynhuxtable.heroes.service.bean.HeroFilterResult;
import org.kathrynhuxtable.heroes.service.bean.UIFilter;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterMatchMode;
import org.kathrynhuxtable.heroes.service.bean.UIFilter.UIFilterMetadata;
import org.kathrynhuxtable.heroes.service.persistence.HeroDAO;
import org.kathrynhuxtable.heroes.service.persistence.domain.HeroDO;

@Slf4j
@Component
@AllArgsConstructor
public class HeroService {

	private final HeroDAO heroDao;

	public HeroFilterResult find(UIFilter filter) {
		log.info("Filter: " + filter.toString());
		long totalRecords = heroDao.countByFilter(filter);
		List<HeroDO> heroes = heroDao.findByFilter(filter);

		HeroFilterResult result = new HeroFilterResult();
		result.setHeroes(toHeroes(heroes));
		result.setTotalRecords((int) totalRecords);
		return result;
	}

	public List<Hero> findTopHeroes() {
		return toHeroes(heroDao.findTopHeroes(5));
	}

	public List<Hero> search(String name) {
		UIFilter filter = new UIFilter();
		UIFilterMetadata md = new UIFilterMetadata();
		md.setValue(name);
		md.setMatchMode(UIFilterMatchMode.contains);
		Map<String, List<UIFilterMetadata>> filterMap = new HashMap<>();
		filterMap.put("name", Collections.singletonList(md));
		filter.setFilters(filterMap);
		return toHeroes(heroDao.findByFilter(filter));
	}

	public Hero find(long id) {
		log.info("Searching for hero " + id);
		Optional<HeroDO> heroDO = heroDao.findById(id);
		return heroDO.map(this::toHero).orElse(null);
	}

	public Hero update(Hero hero) {
		return toHero(heroDao.save(toHeroDO(hero)));
	}

	public Hero save(Hero hero) {
		return toHero(heroDao.save(toHeroDO(hero)));
	}

	public Hero delete(long id) {
		Optional<HeroDO> found = heroDao.findById(id);
        found.ifPresent(heroDO -> heroDao.delete(heroDO));
		return found.map(this::toHero).orElse(null);
	}

	private List<Hero> toHeroes(List<HeroDO> heroDOs) {
		return heroDOs.stream().map(this::toHero).collect(Collectors.toList());
	}

	private Hero toHero(HeroDO heroDO) {
		if (heroDO == null) {
			return null;
		} else {
			return Hero.builder().id(heroDO.getId()).name(heroDO.getName()).power(heroDO.getPower())
					.alterEgo(heroDO.getAlterEgo()).rating(heroDO.getRating()).build();
		}
	}

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
			return heroDO;
		}
	}
}
