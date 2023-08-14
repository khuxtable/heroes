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

    themeOptions: any[] = [
        {label: 'Light', value: 'viva-light'},
        {label: 'Dark', value: 'viva-dark'}
    ];

    user: User | null = null;

    value: string = 'viva-light';

    constructor(private authService: AuthService,
                private themeService: ThemeService,
                private userService: UserService) {
        authService.getLoggedInName.subscribe(user => {
            this.user = user;
            if (this.user?.preferredTheme) {
                this.value = this.user.preferredTheme;
                this.themeService.switchTheme(this.value);
            }
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

    logout(): void {
        this.authService.logout();
    }

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
