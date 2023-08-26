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
import { ActivatedRoute } from '@angular/router';
import { Location } from '@angular/common';

import { Hero } from '@appModel/hero';
import { HeroService } from '@appServices/hero.service';
import { AuthService } from '@appServices/auth.service';

/**
 * Display the details for a particular hero. Conditionally allows editing,
 * or creation of a new hero.
 */
@Component({
	selector: 'toh-hero-detail',
	templateUrl: './hero-detail.component.html',
	styleUrls: ['./hero-detail.component.scss']
})
export class HeroDetailComponent {

	hero: Hero | undefined;
	editable: boolean = false;

	// Initial list of powers. The control allows free text, but these are presented as options.
	powers = [
		'Really Smart',
		'Super Flexible',
		'Super Hot',
		'Weather Changer',
	];

	constructor(
		private route: ActivatedRoute,
		private heroService: HeroService,
		private authService: AuthService,
		private location: Location
	) {
	}

	ngOnInit(): void {
		this.getHero();
		this.getUser();
	}

	/**
	 * Load the current hero.
	 */
	getHero(): void {
		let idParam = this.route.snapshot.paramMap.get('id');
		if (!idParam) {
			this.hero = {id: 0, name: '', power: '', rating: 0};
		} else {
			const id = Number(idParam);
			this.heroService.getHero(id)
				.subscribe(hero => this.hero = hero);
		}
	}

	/**
	 * Load the current user and determine whether they can make changes.
	 */
	getUser(): void {
		if (this.authService.userValue) {
			this.editable = this.authService.userValue.privileges.includes('ADMIN');
		} else {
			this.editable = false;
		}
	}

	/**
	 * Save any changes.
	 */
	save(): void {
		if (this.hero) {
			if (this.hero.id == 0) {
				this.heroService.addHero(this.hero)
					.subscribe(() => this.goBack());
			} else {
				this.heroService.updateHero(this.hero)
					.subscribe(() => this.goBack());
			}
		}
	}

	/**
	 * Return to the previous page.
	 */
	goBack(): void {
		this.location.back();
	}
}
