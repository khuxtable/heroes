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

import { Component, OnInit, signal, WritableSignal } from '@angular/core';
import { AppState } from "@app/state/app.reducer";
import { getUser } from "@app/state/auth/auth.selector";
import { User } from '@appModel/user';
import { AuthService } from '@appServices/auth.service';
import * as AuthAction from '@app/state/auth/auth.actions';
import { ThemeService } from "@appServices/theme.service";
import { Store } from "@ngrx/store";

/**
 * The main app component. Displays a theme selection and the logged in user,
 * allows switching between the Dashboard and Heroes components, and
 * displays the Message component.
 */
@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.scss']
})
export class AppComponent implements OnInit {
	title = 'Tour of Heroes';

	// Current user
	user: WritableSignal<User | null> = signal(null);

	// Current theme
	currentTheme: WritableSignal<string> = signal('Light');

	constructor(private authService: AuthService,
							private store$: Store<AppState>,
	            private themeService: ThemeService) {
		// Ask for the currently logged in user. Load user and theme
	}

	ngOnInit(): void {
		this.store$.select(getUser).subscribe(user => {
			this.user.set(user);
			let theme: string;
			if (this.curUser) {
				theme = this.curUser ? this.curUser.preferredTheme : 'Light';
				this.currentTheme.set(theme);
			} else {
				theme = "Login";
			}
			if (this.curUser?.preferredTheme) {
				this.currentTheme.set(this.curUser.preferredTheme);
			}
			this.themeService.switchTheme(theme);
		});
	}

	get curUser(): User | null {
		return this.user();
	}

	/**
	 * Perform logout action.
	 */
	logout(): void {
		// Switch to login theme and clear user.
		this.themeService.switchTheme("Login");
		this.store$.dispatch(AuthAction.logOutUser());
	}
}
