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
package org.kathrynhuxtable.heroes.endpoints.controller;

import java.util.List;

import org.kathrynhuxtable.heroes.endpoints.bean.Hero;
import org.kathrynhuxtable.heroes.endpoints.bean.HeroFilterResult;
import org.kathrynhuxtable.heroes.endpoints.bean.UIFilter;
import org.kathrynhuxtable.heroes.endpoints.service.HeroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("hero")
public class HeroController {

	@Autowired
	private HeroService heroService;

	@GetMapping(path = "/search", produces = "application/json")
	public @ResponseBody List<Hero> search(@RequestParam(name = "name") String name) {
		return heroService.search(name);
	}

	@GetMapping(path = "/top", produces = "application/json")
	public @ResponseBody List<Hero> findTopHeroes() {
		return heroService.findTopHeroes();
	}

	@GetMapping(path = "/{id}", produces = "application/json")
	public @ResponseBody Hero find(@PathVariable int id) {
		return heroService.find(id);
	}

	@PostMapping(path = "/filter", produces = "application/json")
	public @ResponseBody HeroFilterResult filterFind(@RequestBody UIFilter filter) {
		return heroService.find(filter);
	}

	@PutMapping
	public @ResponseBody Hero save(@RequestBody Hero newHero) {
		Hero hero;
		if (newHero.getId() == null) {
			hero = heroService.save(newHero);
		} else {
			hero = heroService.find(newHero.getId());
			if (hero != null) {
				hero.setName(newHero.getName());
				hero.setPower(newHero.getPower());
				hero.setAlterEgo(newHero.getAlterEgo());
				hero = heroService.update(hero);
			} else {
				hero = null;
			}
		}
		return hero;
	}

	@DeleteMapping(path = "/{id}", produces = "application/json")
	public @ResponseBody Hero delete(@PathVariable int id) {
		return heroService.delete(id);
	}

	@GetMapping(path = "initHeroes", produces = "application/json")
	public @ResponseBody Hero initHeroes() {
		heroService.initHeroes();
		return heroService.find(1L);
	}
}