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

import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from "@appServices/user.service";
import { concatMap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private serviceUrl = '/api/auth';  // URL to web api

  httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/x-www-form-urlencoded',
      'Accept': 'application/json'
    })
  };

  constructor(private router: Router,
              private http: HttpClient,
              private userService: UserService) {
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
    return this.http.post<string>(`${this.serviceUrl}/login`,
      `username=${username}&password=${password}`, this.httpOptions)
      .pipe(concatMap(username => this.userService.findByUsername(username))
      );
  }
  //
  // /**
  //  * Log the user out. Return to the login screen.
  //  */
  // logout() {
  //   // remove user from local storage to log user out
  //   sessionStorage.removeItem('user');
  //   this.userSubject.next(null);
  //   this.router.navigate(['/login']);
  // }
}
