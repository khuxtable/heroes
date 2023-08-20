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

import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { Hero } from '@appModel/hero';
import { HeroService } from '@appServices/hero.service';

/**
 * The "dashboard" page, which is the logged in home screen.
 * Displays the top five rated heroes, and allows searching by name.
 */
@Component({
	selector: 'app-dashboard',
	templateUrl: './dashboard.component.html',
	styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {

	heroes: Hero[] = [];

	constructor(
		private router: Router,
		private heroService: HeroService) {
	}

	ngOnInit(): void {
		this.getHeroes();
	}

	/**
	 * Load the top-rated heroes.
	 */
	getHeroes(): void {
		this.heroService.getTopHeroes()
			.subscribe(result => this.heroes = result);
	}

	/**
	 * Display the details for a hero.
	 *
	 * @param id the id of the hero to display.
	 */
	routeMe(id: number): void {
		this.router.navigate(['/detail/' + id]);
	}
}
