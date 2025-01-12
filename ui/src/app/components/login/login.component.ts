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
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup } from '@angular/forms';
import { AppState } from "@app/state/app.reducer";
import { logInUser } from "@app/state/auth/auth.actions";
import { getErrMessage, getUser } from "@app/state/auth/auth.selector";
import { User } from "@appModel/user";

import { AuthService } from '@appServices/auth.service';
import { Store } from "@ngrx/store";
import { Observable, of } from "rxjs";

/**
 * The login component. Displays the login request and handles the user login.
 */
@Component({
  selector: 'toh-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
  authData = { username: '', password: '' };
  error$: Observable<string> = of('');
  loggedInUser$: Observable<User | null> = of(null);

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private store$: Store<AppState>
  ) {
  }

  ngOnInit() {
    this.loggedInUser$ = this.store$.select(getUser);
    this.error$ = this.store$.select(getErrMessage);

    this.loggedInUser$.subscribe(user => {
      if (user != null) {
        const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
        this.router.navigate([returnUrl]);
      }
    });

    // redirect to home if already logged in
    // if (this.authService.userValue) {
    //   this.router.navigate(['/']);
    // } else {
    //   this.authService.getLoggedInName.emit(null);
    // }
  }

  /**
   * Validate the credentials with the authentication service.
   */
  onSubmit() {
    console.log("login in");
    this.store$.dispatch(logInUser({ username: this.authData.username, password: this.authData.password }));
    // this.authService.login(this.authData.username, this.authData.password)
    //   .subscribe(() => {
    //     if (this.authService.userValue) {
    //       this.error = "";
    //       // get return url from route parameters or default to '/'
    //       const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
    //       this.router.navigate([returnUrl]);
    //     }
    //   });
  }
}
