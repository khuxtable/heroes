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

import { statsErrorsToString } from "@angular-devkit/build-angular/src/tools/webpack/utils/stats";
import { AUTH_STORE_KEY, AuthState } from "./auth.reducer";
import { createFeatureSelector, createSelector } from "@ngrx/store";

export const selectAuthState = createFeatureSelector<AuthState>(AUTH_STORE_KEY);

export const getUser = createSelector(
  selectAuthState,
  state => state.user
)

export const getErrorMessage = createSelector(
  selectAuthState,
  state => state.errMessage
)

export const getPrivileges = createSelector(
  getUser,
  user => user == null ? [] : user.privileges
)

export const getErrMessage = createSelector(
  selectAuthState,
  state => state.errMessage
)
