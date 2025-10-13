import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";

import { catchError } from "rxjs";

import { environment } from "@env/environment";
import { Subject as SubjectInterface } from "@app/pages/interfaces/Subject.interface";

@Injectable({
    providedIn: 'root'
})
export class SubjectsService {
    constructor(private http: HttpClient) { }

    getAll(): Observable<SubjectInterface[]> {
        return this.http.get<SubjectInterface[]>(`${environment.apiUrl}/subjects`).pipe(
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}