import { Component, OnInit } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { FormGroup } from '@angular/forms';

import { AuthService } from '@appServices/auth.service';
import { Authdata } from '@appModel/authdata';

@Component({
	selector: 'login-component',
	templateUrl: './login.component.html',
	styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	authdata = new Authdata('', '');
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

    onSubmit() {
        this.error = 'Invalid login';
        this.authService.login(this.authdata.username, this.authdata.password)
            .subscribe(() => {
					this.error = '';
                    // get return url from route parameters or default to '/'
                    const returnUrl = this.route.snapshot.queryParams['returnUrl'] || '';
                    this.router.navigate([returnUrl]);
                });
    }
}