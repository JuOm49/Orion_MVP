import { Injectable } from "@angular/core";

import { BehaviorSubject } from "rxjs";

import { User } from "@core/interfaces/user.interface";

@Injectable({
    providedIn: "root"
})
export class SessionService {
    public isLogged = false;
    public user: User | undefined;

    private isLoggedBehaviorSubject = new BehaviorSubject<boolean>(this.isLogged);

    constructor() {
        const token = localStorage.getItem('token');
        if (token) {
            this.isLogged = true;
        }
        this.nextLogged();
    }

    public login(user: User): void {
        this.user = user;
        this.isLogged = true;
        this.nextLogged();
    }

    public logout(): void {
        localStorage.removeItem('token');
        this.user = undefined;
        this.isLogged = false;
        this.nextLogged();
    }

    public getIsLoggedBehaviorSubjectAsObservable() {
        return this.isLoggedBehaviorSubject.asObservable();
    }
    
    private nextLogged(): void {
        this.isLoggedBehaviorSubject.next(this.isLogged);
    }
}