import { Component, EventEmitter, Input, Output, signal, WritableSignal } from '@angular/core';
import { User } from "@appModel/user";
import { ThemeService } from "@appServices/theme.service";
import { UserService } from "@appServices/user.service";

@Component({
	selector: 'toh-user-profile',
	templateUrl: './user-profile.component.html',
	styleUrls: ['./user-profile.component.scss']
})
export class UserProfileComponent {
	@Input({required: true}) user: WritableSignal<User | null> = signal(null);
	@Input({required: true}) currentTheme!: WritableSignal<string>;
	@Output() logoutEvent = new EventEmitter<string>();

	// Theme button options
	themeOptions: string[] = ['Light', 'Dark'];

	constructor(private userService: UserService,
	            private themeService: ThemeService) {
	}

	get avatar(): string {
		return `/api/avatar/image/${this.curUser?.id}`;
	}

	get curUser(): User | null {
		return this.user();
	}

	/**
	 * Perform theme change action. Saves the selection in the user's profile.
	 *
	 * @param theme the theme selected, currently an actual PrimeNG theme name.
	 */
	changeTheme(theme: string) {
		if (this.curUser) {
			this.userService.updateTheme(this.curUser.id, theme).subscribe(
				user => {
					if (user) {
						this.user.set(user);
						this.themeService.switchTheme(user.preferredTheme);
					}
				}
			);
		}
	}
}
