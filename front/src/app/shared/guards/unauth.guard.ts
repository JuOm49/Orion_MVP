import { Injectable } from "@angular/core";
import { CanActivate, Router } from "@angular/router";

import { SessionService } from "@shared/services/session.service";

@Injectable({ providedIn: 'root' })
export class UnauthGuard implements CanActivate {
    
    constructor(private router: Router, private sessionService: SessionService) {}

    // Guard to protect routes that should be inaccessible to authenticated users
    public canActivate(): boolean {
        if(this.sessionService.isLogged) {
            this.router.navigate(['posts']);
            return false;
        }
        return true;
    }
}