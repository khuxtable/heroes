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
import { createAction, props } from "@ngrx/store";

export const setSearchTerms = createAction(
  '[Heroes] Set Search Terms',
  props<{
    searchTerms: string
  }>()
);

export const loadSearchResults = createAction(
  '[Heroes] Load Search Results',
  props<{
    searchTerms: string
  }>()
);

export const searchResultsSuccess = createAction(
  '[Heroes] Search Results Success',
  props<{
    heroes: Hero[]
  }>()
);

export const searchResultsFailure = createAction(
  '[Heroes] Search Results Failure',
  props<{
    errMessage: string
  }>()
);
