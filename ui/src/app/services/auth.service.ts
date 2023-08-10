import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Router } from '@angular/router';
import { catchError, tap } from 'rxjs/operators';
import { Output, EventEmitter } from '@angular/core';

import { MessageService } from '@appServices/message.service';
import { User } from '@appModel/user';

@Injectable({
	providedIn: 'root',
})
export class AuthService {

	private serviceUrl = '/api/auth';  // URL to web api

	private userSubject: BehaviorSubject<User | null>;
	public user: Observable<User | null>;
	@Output() getLoggedInName: EventEmitter<any> = new EventEmitter();

	httpOptions = {
		headers: new HttpHeaders({ 'Content-Type': 'application/json' })
	};

	constructor(private router: Router, private http: HttpClient, private messageService: MessageService) {
		this.userSubject = new BehaviorSubject(JSON.parse(sessionStorage.getItem('user')!));
		this.user = this.userSubject.asObservable();
	}

	public get userValue() {
		return this.userSubject.value;
	}

	login(username: string, password: string) : Observable<User> {
		return this.http.put<User>(`${this.serviceUrl}/login`, { username: username, password: password }, this.httpOptions)
			.pipe(tap(user => {
				// store user details and basic auth credentials in local storage to keep user logged in between page refreshes
				sessionStorage.setItem('user', JSON.stringify(user));
				this.userSubject.next(user);
				this.getLoggedInName.emit(user);
				return user;
			}));
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