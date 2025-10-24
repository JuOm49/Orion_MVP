import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";
import { SessionService } from "../services/session.service";

@Injectable({ providedIn: 'root' })
export class AuthGuard implements CanActivate {

    constructor(private sessionService: SessionService, private router: Router) {}

    // Guard to protect routes that require authentication
    public canActivate(): boolean {
    if (!this.sessionService.isLogged) {
      this.router.navigate(['/home']);
      return false;
    }
    return true;
  }
}