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
import { createAction, props } from "@ngrx/store";

export const logInUser = createAction(
  '[Auth] Log in User',
  props<{
    username: string,
    password: string
  }>()
);

export const loginSuccess = createAction(
  '[Auth] Login Success',
  props<{
    user: User
  }>()
);

export const loginFailure = createAction(
  '[Auth] Login Failure',
  props<{
    errMessage: string
  }>()
);

export const logOutUser = createAction(
  '[Auth] Log Out User'
)

export const logoutUserSuccess = createAction(
  '[Auth] Log Out User Success'
)
