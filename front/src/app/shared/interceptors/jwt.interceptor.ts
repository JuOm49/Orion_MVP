import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class JwtInterceptor implements HttpInterceptor {

  public intercept(request: HttpRequest<any>, next: HttpHandler) {
    
    const excludedUrls = ['/api/login', '/api/register'];
    const shouldExclude = excludedUrls.some(url => request.url.includes(url));
    
    if (!shouldExclude) {
      const token = localStorage.getItem('token');
      if (token) {
        request = request.clone({
          setHeaders: {
            Authorization: `Bearer ${token}`,
          },
        });
      }
    }
    
    return next.handle(request);
  }
}