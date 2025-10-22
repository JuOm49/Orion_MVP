import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError, Observable, take } from "rxjs";

import { Subscription as subscribeToSubject } from "@pages/interfaces/Subscription.interface";

import { environment } from "@env/environment";

@Injectable({
    providedIn: 'root'
})
export class SubscriptionService {
    constructor(private http: HttpClient) { }

    getAllSubscribedSubjectsForUser(): Observable<subscribeToSubject[]> {
        return this.http.get<subscribeToSubject[]>(`${environment.apiUrl}/subscriptions/user`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error fetching subscribed subjects:', error);
                }
                throw error;
            })
        );
    }


    subscribeToSubject(subjectId: number): Observable<subscribeToSubject> {
        return this.http.post<subscribeToSubject>(`${environment.apiUrl}/subscriptions`, subjectId).pipe(
            take(1),
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error subscribing to subject:', error);
                }
                throw error;
            })
        );
    }

    unsubscribeFromSubject(subjectId: number) {
        return this.http.delete(`${environment.apiUrl}/subscriptions/${subjectId}`).pipe(
            take(1),
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error unsubscribing from subject:', error);
                }
                throw error;
            })
        );
    }
}