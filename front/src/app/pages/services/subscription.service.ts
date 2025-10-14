import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError, take, tap } from "rxjs";

import { Subscription as subscribeToSubject } from "@pages/interfaces/Subscription.interface";

import { environment } from "@env/environment";

@Injectable({
    providedIn: 'root'
})
export class SubscriptionService {
    constructor(private http: HttpClient) { }

    getAllSubscribedSubjectsForUser() {
        return this.http.get<subscribeToSubject[]>(`${environment.apiUrl}/subscriptions/user`).pipe(
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }


    subscribeToSubject(subjectId: number) {
        return this.http.post<subscribeToSubject>(`${environment.apiUrl}/subscriptions`, subjectId).pipe(
            take(1),
            tap(() => {
                this.getAllSubscribedSubjectsForUser();
            }),
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}