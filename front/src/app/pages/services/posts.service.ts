import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError, Observable } from "rxjs";

import { environment } from "@env/environment";
import { Post } from "../interfaces/Post.interface";

@Injectable({
    providedIn: 'root'
})
export class PostsService {
    constructor( private http: HttpClient ) { }

    getSubscribedPostsForUser(): Observable<Post[]> {
        return this.http.get<Post[]>(`${environment.apiUrl}/posts/subscribed`).pipe(
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}