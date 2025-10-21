import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { catchError, Observable } from "rxjs";

import { environment } from "@env/environment";

import { RegisterRequest } from "@pages/interfaces/RegisterRequest.interface";
import { AuthSuccess } from "@pages/interfaces/AuthSuccess.interface";
import { LoginRequest } from "@pages/interfaces/LoginRequest.interface";
import { User } from "@app/core/interfaces/user.interface";

@Injectable(
    { providedIn: 'root' }
)
export class AuthService {

    constructor(private httpClient: HttpClient) {}

    public saveUser(formValue: RegisterRequest): Observable<AuthSuccess> {
        return this.httpClient.post<AuthSuccess>(`${environment.apiUrl}/register`, formValue).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }

    public updateUser(formValue: RegisterRequest): Observable<AuthSuccess> {
        return this.httpClient.put<AuthSuccess>(`${environment.apiUrl}/user`, formValue).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }

    public loginUser(loginRequest: LoginRequest): Observable<AuthSuccess> {
        return this.httpClient.post<AuthSuccess>(`${environment.apiUrl}/login`, loginRequest).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Unauthorized or forbidden:', error);
                    
                }
                throw error;
            })
        );
    }

    public getUser(): Observable<User> {
        return this.httpClient.get<User>(`${environment.apiUrl}/currentUser`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}