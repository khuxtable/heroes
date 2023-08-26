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

import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup } from '@angular/forms';

import { AuthService } from '@appServices/auth.service';

/**
 * The login component. Displays the login request and handles the user login.
 */
@Component({
	selector: 'toh-login',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {
	authData = {username: '', password: ''};
	loginForm!: FormGroup;
	error = '';

	constructor(
		private route: ActivatedRoute,
		private router: Router,
		private authService: AuthService
	) {
		// redirect to home if already logged in
		if (this.authService.userValue) {
			this.router.navigate(['/']);
		} else {
			this.authService.getLoggedInName.emit(null);
		}
	}

	ngOnInit() {
	}

	/**
	 * Validate the credentials with the authentication service.
	 */
	onSubmit() {
		this.error = 'Invalid login';
		this.authService.login(this.authData.username, this.authData.password)
			.subscribe(() => {
				if (this.authService.userValue) {
					this.error = "";
					// get return url from route parameters or default to '/'
					const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
					this.router.navigate([returnUrl]);
				}
			});
	}
}