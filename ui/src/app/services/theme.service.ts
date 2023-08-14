import {Inject, Injectable} from '@angular/core';
import {DOCUMENT} from '@angular/common';
import {BehaviorSubject, Observable} from "rxjs";

import {MessageService} from '@appServices/message.service';

@Injectable({
    providedIn: 'root',
})
export class ThemeService {

    private themeSubject: BehaviorSubject<string | null>;

    constructor(@Inject(DOCUMENT) private document: Document,
                private messageService: MessageService) {
        this.themeSubject = new BehaviorSubject(JSON.parse(sessionStorage.getItem('theme')!));
    }

    public get themeValue() {
        return this.themeSubject.value;
    }

    switchTheme(theme: string) {
        sessionStorage.setItem('theme', JSON.stringify(theme));
        let themeLink = this.document.getElementById('app-theme') as HTMLLinkElement;

        if (themeLink) {
            themeLink.href = theme + '.css';
        }
        this.log(`Set theme to ${theme}`);
    }

    /** Log a HeroService message with the MessageService */
    private log(message: string) {
        this.messageService.add(`ThemeService: ${message}`);
    }
}
