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
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}