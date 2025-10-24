import { HttpHandler, HttpInterceptor, HttpRequest } from "@angular/common/http";
import { Injectable } from "@angular/core";

@Injectable({ providedIn: 'root' })
export class JwtInterceptor implements HttpInterceptor {
  // Interceptor to add JWT token to all HTTP requests except for login and registration
  public intercept(request: HttpRequest<unknown>, next: HttpHandler) {
    
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