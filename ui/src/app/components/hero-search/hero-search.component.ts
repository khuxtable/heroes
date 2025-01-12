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
import { AppState } from "@app/state/app.reducer";
import { loadSearchResults, setSearchTerms } from "@app/state/heroes/heroes.actions";
import { getErrorMessage, getHeroes, getSearchTerms } from "@app/state/heroes/heroes.selector";

import { Hero } from '@appModel/hero';
import { HeroService } from '@appServices/hero.service';
import { Store } from "@ngrx/store";

import { Observable, of } from 'rxjs';

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
	heroes$: Observable<Hero[]>;
	heroes: Hero[] = [];
	selectedHero: Hero | null = null;
	searchTerms$: Observable<string>;
	errMessage$: Observable<string>;

	constructor(private heroService: HeroService, private store$ : Store<AppState>) {
		this.searchTerms$ = of('');
		this.heroes$ = of([]);
		this.errMessage$ = of('');
	}

	ngOnInit(): void {
		this.searchTerms$ = this.store$.select(getSearchTerms);
		this.heroes$ = this.store$.select(getHeroes);
		this.errMessage$ = this.store$.select(getErrorMessage);
	}

	search(searchTerms: string) {
		this.store$.dispatch(setSearchTerms({ searchTerms }));
		this.store$.dispatch(loadSearchResults({ searchTerms }));
	}
}
