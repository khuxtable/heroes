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
import {Observable, of} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, tap} from 'rxjs/operators';

import {User} from '@appModel/user';
import {MessageService} from '@appServices/message.service';

@Injectable({
    providedIn: 'root',
})
export class UserService {

    private serviceUrl = '/api/user';  // URL to web api

    httpOptions = {
        headers: new HttpHeaders({'Content-Type': 'application/json'})
    };

    constructor(private http: HttpClient, private messageService: MessageService) {
    }

    findByUsername(username: string): Observable<User> {
        // For now, assume that a hero with the specified `id` always exists.
        // Error handling will be added in the next step of the tutorial.
        const url = `${this.serviceUrl}/username/${username}`;
        return this.http.get<User>(url).pipe(
            tap(_ => this.log(`find user id=${username}`)),
            catchError(this.handleError<User>(`find id=${username}`))
        );
    }

    updateTheme(id: number, theme: string): Observable<User> {
        // For now, assume that a hero with the specified `id` always exists.
        // Error handling will be added in the next step of the tutorial.
        const url = `${this.serviceUrl}/updateTheme/${id}?theme=${theme}`;
        return this.http.get<User>(url).pipe(
            tap(_ => this.log(`updated theme for id=${id} to ${theme}`)),
            catchError(this.handleError<User>(`updateTheme id=${id}`))
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
        this.messageService.add(`UserService: ${message}`);
    }
}