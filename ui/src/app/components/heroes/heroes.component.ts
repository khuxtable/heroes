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

import { Component, ViewChild } from '@angular/core';
import { Table } from 'primeng/table';
import { TableLazyLoadEvent } from 'primeng/table';
import { Router } from '@angular/router';

import { Hero } from '@appModel/hero';
import { UIFilter } from '@appModel/ui.filter';
import { HeroService } from '@appServices/hero.service';
import { AuthService } from '@appServices/auth.service';

/**
 * The heroes component. Displays a paginated table of heroes, supporting sorting and filtering.
 * Has a button to add a new hero, and the ability to edit and delete heroes.
 */
@Component({
	selector: 'toh-heroes',
	templateUrl: './heroes.component.html',
	styleUrls: ['./heroes.component.scss']
})
export class HeroesComponent {

	heroes: Hero[] = [];
	selectedHeroes: Hero[] = [];
	canAdd: boolean = false;
	loading: boolean = true;
	totalRecords: number = 0;

	@ViewChild('dt')
	dt: Table | undefined;

	constructor(private router: Router,
	            private heroService: HeroService,
	            private authService: AuthService) {
	}

	ngOnInit(): void {
		this.getUser();
		this.loading = true;
	}

	/**
	 * Convert HTML element to its value.
	 * @param $event the event driving the filtering.
	 * @param stringVal the value of the global filter text.
	 */
	applyFilterGlobal($event: any, stringVal: any) {
		this.dt!.filterGlobal(($event.target as HTMLInputElement).value, stringVal);
	}

	/**
	 * Respond to a sort, paginate, or filter event by "lazy loading" the heroes.
	 *
	 * @param event the event containing the selection data.
	 */
	loadHeroes(event: TableLazyLoadEvent) {
		this.loading = true;

		setTimeout(() => {
			this.heroService.getHeroesLazy(new UIFilter(event)).subscribe(res => {
				this.heroes = res.heroes;
				this.totalRecords = res.totalRecords;
				this.loading = false;
			})
		}, 1000);
	}

	/**
	 * Get the current user and set whether they can make changes.
	 */
	getUser(): void {
		if (this.authService.userValue) {
			this.canAdd = this.authService.userValue.privileges.includes('ADMIN');
		} else {
			this.canAdd = false;
		}
	}

	/**
	 * Add a hero. Routes to the "detail" page with a blank hero loaded.
	 */
	add(): void {
		this.router.navigate(['/detail/new']);
	}

	/**
	 * Delete a hero.
	 *
	 * @param hero the hero to delete.
	 */
	delete(hero: Hero): void {
		this.heroes = this.heroes.filter(h => h !== hero);
		this.heroService.deleteHero(hero.id).subscribe();
	}
}
