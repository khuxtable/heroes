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

import { User } from "@appModel/user";
import { createReducer, on } from "@ngrx/store";
import * as AuthAction from './auth.actions';

export const AUTH_STORE_KEY = "authStore";

export interface AuthState {
  user: User | null,
  errMessage: string
}

export const initialAuthState: AuthState = {
  user: null,
  errMessage: ''
}

export const authReducer = createReducer(
  initialAuthState,

  on(AuthAction.loginSuccess, (state, action): AuthState => {
    return {
      ...state,
      user: action.user,
      errMessage: ''
    };
  }),

  on(AuthAction.loginFailure, (state, action): AuthState => {
    return {
      ...state,
      user: null,
      errMessage: action.errMessage
    };
  }),

  on(AuthAction.logoutUserSuccess, (state): AuthState => {
    return {
      ...state,
      user: null,
      errMessage: ''
    };
  }),
)
