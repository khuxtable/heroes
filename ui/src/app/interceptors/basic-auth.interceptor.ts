import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

import { AuthService } from '@appServices/auth.service';

@Injectable()
export class BasicAuthInterceptor implements HttpInterceptor {
    constructor(private authService: AuthService) { }

    intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        const user = this.authService.userValue;
        const isLoggedIn = user != null;
//        if (isLoggedIn /*&& isApiUrl*/) {
//            request = request.clone({
//                setHeaders: { 
//                    Authorization: `Basic ${authdata}`
//                }
//            });
//        }

        return next.handle(request);
    }
}