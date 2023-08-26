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

import { Component, OnInit } from '@angular/core';

import { Subject, ReplaySubject } from 'rxjs';

import {
	debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { Hero } from '@appModel/hero';
import { HeroService } from '@appServices/hero.service';

/**
 * The hero search component. Displays a text search box and displays a list of
 * heroes whose names match the text, ignoring case. If the search box is empty,
 * the results are empty. The hero names are clickable and will route to the details
 * page for that hero.
 */
@Component({
	selector: 'toh-hero-search',
	templateUrl: './hero-search.component.html',
	styleUrls: ['./hero-search.component.scss']
})
export class HeroSearchComponent implements OnInit {
	heroes$: ReplaySubject<Hero[]> = new ReplaySubject(1);
	heroes: Hero[] = [];
	selectedHero: Hero | null = null;
	private searchTerms = new Subject<string>();

	constructor(private heroService: HeroService) {
		this.heroes$.next([]);
		this.heroes$.subscribe(heroes => this.heroes = heroes);
	}

	// Push a search term into the observable stream.
	search(term: string): void {
		this.searchTerms.next(term);
	}

	ngOnInit(): void {
		this.heroes$.next([]);
		this.searchTerms.pipe(
			// wait 300ms after each keystroke before considering the term
			debounceTime(300),

			// ignore new term if same as previous term
			distinctUntilChanged(),

			// switch to new search observable each time the term changes
			switchMap((term: string) => this.heroService.searchHeroes(term))
		).subscribe(heroes => this.heroes$.next(heroes));
	}
}