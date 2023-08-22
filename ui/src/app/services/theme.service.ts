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

import { DOCUMENT } from '@angular/common';
import { Inject, Injectable } from '@angular/core';

import { MessageService } from '@appServices/message.service';

@Injectable({
	providedIn: 'root',
})
export class ThemeService {
	private themeMap: { [key: string]: string } = {
		"Login": 'lara-light-teal',
		"Light": 'viva-light',
		"Dark": 'viva-dark',
	};

	constructor(@Inject(DOCUMENT) private document: Document,
	            private messageService: MessageService) {
	}

	/**
	 * Switch the page them to the requested them.
	 *
	 * @param theme the new theme, which is currently a PrimeNG theme name.
	 */
	switchTheme(theme: string) {
		let themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;

		if (themeLink) {
			themeLink.href = this.themeMap[theme] + '.css';
		}
		this.log(`Set theme to ${theme}`);
	}

	/** Log a HeroService message with the MessageService */
	private log(message: string) {
		this.messageService.add(`ThemeService: ${message}`);
	}
}
