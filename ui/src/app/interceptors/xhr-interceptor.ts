import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest } from '@angular/common/http';
import { Observable } from 'rxjs';

export class XhrHeaderInterceptor implements HttpInterceptor {

	intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
		const clonedRequest = req.clone({ headers: req.headers.append('X-Requested-With', 'XMLHttpRequest') });
		return next.handle(clonedRequest);
	}
}