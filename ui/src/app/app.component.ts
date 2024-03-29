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
import { User } from '@appModel/user';
import { AuthService } from '@appServices/auth.service';
import { ThemeService } from "@appServices/theme.service";

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
	            private themeService: ThemeService) {
		// Ask for the currently logged in user. Load user and theme
		authService.getLoggedInName.subscribe(user => {
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

	ngOnInit(): void {
		this.getUser();
	}

	get curUser(): User | null {
		return this.user();
	}

	getUser(): void {
		const curUser = this.authService.userValue;
		if (curUser == null) {
			this.user.set(null);
		} else {
			this.user.set(curUser);
			if (this.curUser?.preferredTheme) {
				this.currentTheme.set(this.curUser.preferredTheme);
			}
			this.themeService.switchTheme(this.currentTheme());
		}
	}

	/**
	 * Perform logout action.
	 */
	logout(): void {
		// Switch to login theme and clear user.
		this.themeService.switchTheme("Login");
		this.user.set(null);
		this.authService.logout();
	}
}
