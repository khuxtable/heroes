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

import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable, of, switchMap} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Router} from '@angular/router';
import {catchError, tap} from 'rxjs/operators';
import {Output, EventEmitter} from '@angular/core';

import {MessageService} from '@appServices/message.service';
import {UserService} from "@appServices/user.service";
import {User} from '@appModel/user';

@Injectable({
	providedIn: 'root',
})
export class AuthService {

	private serviceUrl = '/api/auth';  // URL to web api

	public username: any;
	private userSubject: BehaviorSubject<User | null>;
	public user: Observable<User | null>;
	@Output() getLoggedInName: EventEmitter<any> = new EventEmitter();

	httpOptions = {
		headers: new HttpHeaders({
			'Content-Type': 'application/x-www-form-urlencoded',
			'Accept': 'application/json'
		})
	};

	constructor(private router: Router,
	            private http: HttpClient,
	            private messageService: MessageService,
	            private userService: UserService) {
		this.userSubject = new BehaviorSubject(JSON.parse(sessionStorage.getItem('user')!));
		this.user = this.userSubject.asObservable();
	}

	public get userValue() {
		return this.userSubject.value;
	}

	login(username: string, password: string) {
		const user$ = this.http.post<string>(`${this.serviceUrl}/login`,
			`username=${username}&password=${password}`, this.httpOptions)
			.pipe(tap(username => this.username = username),
				switchMap(username => {
					return this.userService.findByUsername(username);
				}));

		user$.subscribe(user => {
			// store user details and basic auth credentials in local storage to keep user logged in between page refreshes
			sessionStorage.setItem('user', JSON.stringify(user));
			this.userSubject.next(user);
			this.getLoggedInName.emit(user);
			return user;
		});

		return this.user;
	}

	logout() {
		// remove user from local storage to log user out
		sessionStorage.removeItem('user');
		this.userSubject.next(null);
		this.router.navigate(['/login']);
	}

	getAuth(): Observable<User> {
		// For now, assume that a hero with the specified `id` always exists.
		// Error handling will be added in the next step of the tutorial.
		const url = `${this.serviceUrl}/user`;
		return this.http.get<User>(url).pipe(
			tap(_ => this.log(`fetched auth`)),
			catchError(this.handleError<User>('getAuth'))
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

			console.error(error); // log to console instead

			this.log(`${operation} failed: ${error.message}`);

			// Let the app keep running by returning an empty result.
			return of(result as T);
		};
	}

	/** Log a HeroService message with the MessageService */
	private log(message: string) {
		this.messageService.add(`AuthService: ${message}`);
	}
}