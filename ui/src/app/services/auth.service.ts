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

import { EventEmitter, Injectable, Output } from '@angular/core';
import { BehaviorSubject, Observable, switchMap } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { tap } from 'rxjs/operators';

import { MessageService } from '@appServices/message.service';
import { UserService } from "@appServices/user.service";
import { User } from '@appModel/user';

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
		// Load the currently saved logged in user. Should really use a session cookie for this.
		this.userSubject = new BehaviorSubject(JSON.parse(sessionStorage.getItem('user')!));
		this.user = this.userSubject.asObservable();
	}

	/**
	 * Return the logged in user.
	 */
	public get userValue() {
		return this.userSubject.value;
	}

	/**
	 * Present the login credentials to the authentication service on the server.
	 * It will return the username if the credentials match a user, or {@code null} if not.
	 * Then request the user profile from the user service.
	 *
	 * @param username the username.
	 * @param password the password.
	 * @return an <Observable<User | null> for the User profile logged in, or {@code null} if the
	 *         login failed.
	 */
	login(username: string, password: string) {
		const user$ = this.http.post<string>(`${this.serviceUrl}/login`,
			`username=${username}&password=${password}`, this.httpOptions)
			.pipe(tap(username => this.username = username),
				switchMap(username => {
					return this.userService.findByUsername(username);
				}));

		user$.subscribe(user => {
			// store user details and basic auth credentials in local storage to keep
			// user logged in between page refreshes
			sessionStorage.setItem('user', JSON.stringify(user));
			this.userSubject.next(user);
			this.getLoggedInName.emit(user);
			return user;
		});

		return this.user;
	}

	/**
	 * Log the user out. Return to the login screen.
	 */
	logout() {
		// remove user from local storage to log user out
		sessionStorage.removeItem('user');
		this.userSubject.next(null);
		this.router.navigate(['/login']);
	}

	// /**
	//  * Handle Http operation that failed.
	//  * Let the app continue.
	//  *
	//  * @param operation - name of the operation that failed
	//  * @param result - optional value to return as the observable result
	//  */
	// private handleError<T>(operation = 'operation', result?: T) {
	// 	return (error: any): Observable<T> => {
	//
	// 		console.error(error); // log to console instead
	//
	// 		this.log(`${operation} failed: ${error.message}`);
	//
	// 		// Let the app keep running by returning an empty result.
	// 		return of(result as T);
	// 	};
	// }

	// /** Log a HeroService message with the MessageService */
	// private log(message: string) {
	// 	this.messageService.add(`AuthService: ${message}`);
	// }
}