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

import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

export class JsonDateInterceptor implements HttpInterceptor {
	private _isoDateFormat = /^\d{4}-\d{2}-\d{2}T\d{2}:\d{2}:\d{2}(?:\.\d*)?(Z|[-+]\d{2}:\d{2})$/;

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		return next.handle(req).pipe(map((val: HttpEvent<any>) => {
			if (val instanceof HttpResponse) {
				const body = val.body;
				this.convert(body);
			}
			return val;
		}));
	}

	isIsoDateString(value: any): boolean {
		if (value === null || value === undefined) {
			return false;
		}
		if (typeof value === 'string') {
			return this._isoDateFormat.test(value);
		}
		return false;
	}

	convert(body: any) {
		if (body === null || body === undefined) {
			return body;
		}
		if (typeof body !== 'object') {
			return body;
		}
		for (const key of Object.keys(body)) {
			const value = body[key];
			if (this.isIsoDateString(value)) {
				body[key] = new Date(value);
			} else if (typeof value === 'object') {
				this.convert(value);
			}
		}
	}
}