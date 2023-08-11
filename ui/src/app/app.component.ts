import { Component } from '@angular/core';

import { AuthService } from '@appServices/auth.service';
import { User } from '@appModel/user';

@Component({
	selector: 'app-root',
	templateUrl: './app.component.html',
	styleUrls: ['./app.component.css']
})
export class AppComponent {
	title = 'Tour of Heroes';

	user: User | null = null;
	

	constructor(private authService: AuthService) {
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

	logout() : void {
		this.authService.logout();
	}
}
