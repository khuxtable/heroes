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

import {Component} from '@angular/core';

import {AuthService} from '@appServices/auth.service';
import {ThemeService} from "@appServices/theme.service";
import {UserService} from "@appServices/user.service";
import {User} from '@appModel/user';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'Tour of Heroes';

	// Theme button options
	themeOptions: any[] = [
		{label: 'Light', value: 'viva-light'},
		{label: 'Dark', value: 'viva-dark'}
	];

	// Current user
	user: User | null = null;

	// Current theme
	value: string = 'viva-light';

	constructor(private authService: AuthService,
	            private themeService: ThemeService,
	            private userService: UserService) {
		// Ask for the currently logged in user. Load user and theme
		authService.getLoggedInName.subscribe(user => {
			this.user = user;
			var theme : string;
			if (this.user) {
				theme = this.user.preferredTheme ? this.user.preferredTheme : 'viva-light';
				this.value = theme;
			} else {
				theme = 'lara-light-teal';
			}
			if (this.user?.preferredTheme) {
				this.value = this.user.preferredTheme;
			}
			this.themeService.switchTheme(theme);
		});
	}

	ngOnInit(): void {
		this.getUser();
	}

	getUser(): void {
		const curUser = this.authService.userValue;
		if (curUser == null) {
			this.user = null;
		} else {
			this.user = curUser;
		}
	}

	/**
	 * Perform logout action.
	 */
	logout(): void {
		// Switch to login theme and clear user.
		this.themeService.switchTheme('lara-light-teal');
		this.user = null;
		this.authService.logout();
	}

	/**
	 * Perform theme change action. Saves the selection in the user's profile.
	 *
	 * @param theme the theme selected, currently an actual PrimeNG theme name.
	 */
	changeTheme(theme: string) {
		if (this.user) {
			this.userService.updateTheme(this.user.id, theme).subscribe(
				user => {
					if (user) {
						this.user = user;
						this.themeService.switchTheme(user.preferredTheme);
					}
				}
			);
		}
	}
}
