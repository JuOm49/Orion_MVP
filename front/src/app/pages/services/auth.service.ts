import { Injectable } from "@angular/core";
import { HttpClient } from "@angular/common/http";

import { catchError, map, Observable, of } from "rxjs";

import { environment } from "@env/environment";

import { RegisterModel } from "@pages/models/register.model";

@Injectable()
export class AuthService {

    constructor(private httpClient: HttpClient) {}

    saveNewUser(formValue: RegisterModel): Observable<string> {
        return this.httpClient.post(`${environment.apiUrl}/register`, formValue, { 
            responseType: 'text' // Spécifie que la réponse est un texte
        }).pipe(
            map((token: string) => {
                console.log('Token reçu:', token); // Debug
                return token; // Retourne directement le token
            }),
            catchError((error) => {
                console.error('Erreur HTTP:', error); // Debug
                let errorMessage = 'Une erreur est survenue. Veuillez réessayer plus tard.';
                return of(errorMessage);
            })
        );
    }

}