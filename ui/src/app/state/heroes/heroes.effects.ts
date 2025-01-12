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

import { mergeMap, map, catchError, concatMap, debounceTime, distinctUntilChanged, switchMap } from 'rxjs/operators';
import { of } from 'rxjs';
import { HeroService } from '@appServices/hero.service';
import * as HeroesAction from './heroes.actions';

/* NgRx */
import { Actions, createEffect, ofType } from '@ngrx/effects';

@Injectable()
export class HeroesEffects {

  constructor(private actions$: Actions, private heroService: HeroService) { }

  loadHeroes$ = createEffect(() => {
    return this.actions$
      .pipe(
        ofType(HeroesAction.loadSearchResults),
        debounceTime(300),

        // ignore new term if same as previous term
        distinctUntilChanged(),

        switchMap(action => this.heroService.searchHeroes(action.searchTerms)
          .pipe(
            map(heroes => HeroesAction.searchResultsSuccess({ heroes })),
            catchError(errMessage => of(HeroesAction.searchResultsFailure({ errMessage })))
          )
        )
      );
  });
}
