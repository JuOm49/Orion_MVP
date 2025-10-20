import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError, map, Observable } from "rxjs";

import { environment } from "@env/environment";
import { Post } from "../interfaces/Post.interface";
import { NewPost } from "../interfaces/NewPost.interface";

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

    getPostById(postId: number): Observable<Post> {
        return this.http.get<Post>(`${environment.apiUrl}/posts/${postId}`).pipe(
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }


    //change any response
    createPost(newPost: NewPost) {
        return this.http.post<any>(`${environment.apiUrl}/create/post`, newPost).pipe(
            map((response: any) => console.log(response)),
            catchError((error) => {
                if (error.status === 401 || error.status === 403) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}