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

import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, tap } from 'rxjs/operators';

import { Hero } from '@appModel/hero';
import { UIFilter, UIFilterResult } from '@appModel/ui.filter';
import { MessageService } from '@appServices/message.service';

@Injectable({
	providedIn: 'root',
})
export class HeroService {

	private serviceUrl = '/api/hero';  // URL to web api

	httpOptions = {
		headers: new HttpHeaders({'Content-Type': 'application/json'})
	};

	constructor(private http: HttpClient, private messageService: MessageService) {
	}

	/**
	 * A "lazy loader" for the Heroes, returning the requested page, filtered as requested
	 * in sorted order.
	 *
	 * @param filter the filter, containing the filter, pagination, and sorting information.
	 * @return an Observable<UIFilterResult>, which contains the Heroes, and a total count.
	 */
	getHeroesLazy(filter: UIFilter): Observable<UIFilterResult> {
		return this.http.post<UIFilterResult>(this.serviceUrl + "/filter", filter, this.httpOptions).pipe(
			tap(_ => this.log('fetched heroes')),
			catchError(this.handleError<UIFilterResult>('getHeroes', {heroes: [], totalRecords: 0}))
		);
	}

	/**
	 * Retrieve the 5 top-rated heroes.
	 *
	 * @return an Observable<Hero[]> containing the requested Heroes.
	 */
	getTopHeroes(): Observable<Hero[]> {
		return this.http.get<Hero[]>(this.serviceUrl + "/top").pipe(
			tap(_ => this.log('fetched top heroes')),
			catchError(this.handleError<Hero[]>('getTop', []))
		);
	}

	/**
	 * Retrieve a particular Hero by id
	 *
	 * @param id the id of the Hero
	 * @return an Observable<Hero> for the hero requested.
	 */
	getHero(id: number): Observable<Hero> {
		// For now, assume that a hero with the specified `id` always exists.
		// Error handling will be added in the next step of the tutorial.
		const url = `${this.serviceUrl}/${id}`;
		return this.http.get<any>(url).pipe(
			tap(h => {
				this.log(`fetched hero id=${id}`);
				h.powerDate = new Date(h.powerDate);
				return h;
			}),
			catchError(this.handleError<Hero>(`getHero id=${id}`))
		);
	}

	/**
	 * Update the Hero on the server.
	 *
	 * @param hero the Hero to update.
	 */
	updateHero(hero: Hero): Observable<any> {
		this.log('hero:' + JSON.stringify(hero));
		return this.http.put(this.serviceUrl, hero, this.httpOptions).pipe(
			tap(_ => this.log(`updated hero id=${hero.id}`)),
			catchError(this.handleError<any>('updateHero'))
		);
	}

	/**
	 * Add a new Hero on the server.
	 *
	 * @param hero the Hero to add.
	 * @return an Observable<Hero> for the new Hero.
	 */
	addHero(hero: Hero): Observable<Hero> {
		return this.http.put<Hero>(this.serviceUrl, hero, this.httpOptions).pipe(
			tap((newHero: Hero) => this.log(`added hero w/ id=${newHero.id}`)),
			catchError(this.handleError<Hero>('addHero'))
		);
	}

	/**
	 * Delete a Hero from the server.
	 *
	 * @param id the id of the Hero to delete.
	 * @return an Observable<Hero> for the Hero deleted.
	 */
	deleteHero(id: number): Observable<Hero> {
		const url = `${this.serviceUrl}/${id}`;

		return this.http.delete<Hero>(url, this.httpOptions).pipe(
			tap(_ => this.log(`deleted hero id=${id}`)),
			catchError(this.handleError<Hero>('deleteHero'))
		);
	}

	/**
	 * Search for heroes by name. Searches ignoring case, looking for names containing the term.
	 *
	 * @param term the name text to be searched
	 * @return an Observable<Hero[]> containing the matched Heroes.
	 */
	searchHeroes(term: string): Observable<Hero[]> {
		if (!term.trim()) {
			// if not search term, return empty hero array.
			return of([]);
		}

		return this.http.get<Hero[]>(`${this.serviceUrl}/search?name=${term}`).pipe(
			tap(x => x.length ?
				this.log(`found heroes matching "${term}"`) :
				this.log(`no heroes matching "${term}"`)),
			catchError(this.handleError<Hero[]>('searchHeroes', []))
		);
	}

	/**
	 * Handle Http operation that failed.
	 * Let the app continue.
	 *
	 * @param operation - name of the operation that failed
	 * @param result - optional value to return as the observable result
	 */
	private handleError<T>(operation = 'operation', result?: T) {
		return (error: any): Observable<T> => {

			// TODO: send the error to remote logging infrastructure
			console.error(error); // log to console instead

			// TODO: better job of transforming error for user consumption
			this.log(`${operation} failed: ${error.message}`);

			// Let the app keep running by returning an empty result.
			return of(result as T);
		};
	}

	/** Log a HeroService message with the MessageService */
	private log(message: string) {
		this.messageService.add(`HeroService: ${message}`);
	}
}