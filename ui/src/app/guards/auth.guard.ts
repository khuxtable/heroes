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

import { inject, Injectable, OnInit } from '@angular/core';
import { Router, CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';
import { AppState } from "@app/state/app.reducer";
import { getUser } from "@app/state/auth/auth.selector";
import { User } from "@appModel/user";

import { AuthService } from '@appServices/auth.service';
import { Store } from "@ngrx/store";
import { Observable, of, take } from "rxjs";
import { switchMap } from "rxjs/operators";

/**
 * A guard to check for login, and redirect to the login page if not logged in.
 */
@Injectable({ providedIn: 'root' })
export class AuthGuard implements OnInit {

  private loggedInUser$: Observable<User | null> = of(null);

  private router = inject(Router);
  private store$ = inject(Store<AppState>);

  ngOnInit() {
    this.loggedInUser$ = this.store$.select(getUser);
  }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
    return this.store$.select(getUser).pipe(switchMap((user) => {
      if (user) {
        return of(true);
      } else {
        // not logged in so redirect to login page with the return url
        this.router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
        return of(false);
      }
    }));
    // const user = this.store$.value.authStore.user;
    // if (user) {
    // 	// logged in so return true
    // 	return true;
    // } else {
    // 	// not logged in so redirect to login page with the return url
    // 	this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    // 	return false;
    // }
  }
}
