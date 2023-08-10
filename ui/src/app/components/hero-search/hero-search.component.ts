import { Component, OnInit } from '@angular/core';

import { ListboxModule } from 'primeng/listbox';

import { Observable, Subject, of, ReplaySubject } from 'rxjs';

import {
	debounceTime, distinctUntilChanged, switchMap
} from 'rxjs/operators';

import { Hero } from '@appModel/hero';
import { HeroService } from '@appServices/hero.service';

@Component({
	selector: 'app-hero-search',
	templateUrl: './hero-search.component.html',
	styleUrls: ['./hero-search.component.css']
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