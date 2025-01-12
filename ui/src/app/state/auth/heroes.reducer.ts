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

import { Hero } from "@appModel/hero";
import { createReducer, on } from "@ngrx/store";
import * as HeroesAction from './heroes.actions';

export const HEROES_STORE_KEY = "heroesStore";

export interface HeroesState {
  heroes: Hero[],
  searchTerms: string,
  errMessage: string
}

export const initialHeroesState: HeroesState = {
  heroes: [],
  searchTerms: '',
  errMessage: ''
}

export const heroesReducer = createReducer(
  initialHeroesState,

  on(HeroesAction.setSearchTerms, (state, action): HeroesState => {
    return {
      ...state,
      searchTerms: action.searchTerms
    };
  }),
  on(HeroesAction.searchResultsSuccess, (state, action): HeroesState => {
    return {
      ...state,
      heroes: action.heroes,
      errMessage: ''
    };
  }),
  on(HeroesAction.searchResultsFailure, (state, action): HeroesState => {
    return {
      ...state,
      heroes: [],
      errMessage: action.errMessage
    };
  }),
)
