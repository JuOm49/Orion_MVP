import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { Observable, catchError } from "rxjs";

import { Subject as SubjectInterface } from "@pages/interfaces/Subject.interface";

import { environment } from "@env/environment";

@Injectable({
    providedIn: 'root'
})
export class SubjectsService {
    constructor(private http: HttpClient) { }

    getAll(): Observable<SubjectInterface[]> {
        return this.http.get<SubjectInterface[]>(`${environment.apiUrl}/subjects`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error fetching subjects:', error);
                }
                throw error;
            })
        );
    }
}