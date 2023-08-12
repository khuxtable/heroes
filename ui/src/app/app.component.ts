import {Component} from '@angular/core';

import {AuthService} from '@appServices/auth.service';
import {ThemeService} from "@appServices/theme-service";
import {User} from '@appModel/user';

@Component({
    selector: 'app-root',
    templateUrl: './app.component.html',
    styleUrls: ['./app.component.css']
})
export class AppComponent {
    title = 'Tour of Heroes';

    themeOptions: any[] = [
        {label: 'Light', value: 'viva-light'},
        {label: 'Dark', value: 'viva-dark'}
    ];

    user: User | null = null;

    value: string = 'viva-light';

    constructor(private authService: AuthService,
                private themeService: ThemeService) {
        authService.getLoggedInName.subscribe(user => this.user = user);
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

    logout(): void {
        this.authService.logout();
    }

    changeTheme(theme: string) {
        this.themeService.switchTheme(theme);
    }
}
