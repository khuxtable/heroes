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
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

//import { AuthService } from '@appServices/auth.service';

/**
 * Intercept access attempts and check login. Doesn't seem to be used.
 */
@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {
	// constructor(private authService: AuthService) {
	// }

	intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
//		const user = this.authService.userValue;
//		const isLoggedIn = user != null;
//		if (isLoggedIn /*&& isApiUrl*/) {
//			request = request.clone({
//				setHeaders: {
//					Authorization: `Basic ${authdata}`
//				}
//			});
//		}

		return next.handle(request);
	}
}