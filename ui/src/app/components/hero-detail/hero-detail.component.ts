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

import {Component} from '@angular/core';
import {ActivatedRoute} from '@angular/router';
import {Location} from '@angular/common';

import {Hero} from '@appModel/hero';
import {HeroService} from '@appServices/hero.service';
import {AuthService} from '@appServices/auth.service';

@Component({
	selector: 'app-hero-detail',
	templateUrl: './hero-detail.component.html',
	styleUrls: ['./hero-detail.component.css']
})
export class HeroDetailComponent {

	hero: Hero | undefined;
	editable: boolean = false;

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

	getUser(): void {
		if (this.authService.userValue) {
			this.editable = this.authService.userValue.privileges.includes('ADMIN');
		} else {
			this.editable = false;
		}
	}

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

	goBack(): void {
		this.location.back();
	}
}
