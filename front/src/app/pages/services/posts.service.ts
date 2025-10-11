import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError } from "rxjs";

import { environment } from "@env/environment";

@Injectable({
    providedIn: 'root'
})
export class PostsService {
    constructor(private http: HttpClient) { }

    getAll() {
        return this.http.get(`${environment.apiUrl}/posts`).pipe(
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}