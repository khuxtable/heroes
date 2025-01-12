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

import { inject, Injectable } from '@angular/core';
import { ActivatedRoute, Router } from "@angular/router";
import { AuthService } from '@appServices/auth.service';
import { AppState } from "@appState/app.reducer";

/* NgRx */
import { Actions, createEffect, ofType } from '@ngrx/effects';
import { Store } from "@ngrx/store";
import { of } from 'rxjs';

import { catchError, concatMap, map, switchMap, tap } from 'rxjs/operators';
import * as AuthAction from "./auth.actions";

@Injectable()
export class AuthEffects {

  private actions$ = inject(Actions);
  private store$ = inject(Store<AppState>);
  private authService = inject(AuthService);
  private router = inject(Router);
  private activatedRoute = inject(ActivatedRoute);

  logInUser$ = createEffect(() => {
    return this.actions$
      .pipe(
        ofType(AuthAction.logInUser),

        concatMap(action => this.authService.login(action.username, action.password)
          .pipe(
            tap(user => console.log('got user = ' + user)),
            map(
              user => {
                if (user == null) {
                  return AuthAction.loginFailure({ errMessage: 'Invalid credentials' });
                } else {
                  const returnUrl = this.activatedRoute.snapshot.queryParams['returnUrl'] || '';
                  this.router.navigate([returnUrl]);
                  return AuthAction.loginSuccess({ user });
                }
              }),
            catchError(errMessage => of(AuthAction.loginFailure({ errMessage: errMessage + 'x' })))
          )
        ))
  });

  logOutUser$ = createEffect(() => {
    return this.actions$
      .pipe(
        ofType(AuthAction.logOutUser),

        concatMap(() => {
           console.log('Log out user');
           this.router.navigate(['/login']);
           return of(AuthAction.logoutUserSuccess());
          }
        ))
  });
}
