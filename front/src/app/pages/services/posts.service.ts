import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError, Observable, tap } from "rxjs";

import { environment } from "@env/environment";

import { Post } from "@pages/interfaces/Post.interface";
import { NewPost } from "@pages/interfaces/NewPost.interface";
import { CreatedPostResponse } from "@pages/interfaces/CreatedPostResponse.interface";
import { Comment as CommentInterfaces } from "@pages/interfaces/Comment.interface";

@Injectable({
    providedIn: 'root'
})
export class PostsService {
    constructor( private http: HttpClient ) { }

    getFeed(): Observable<Post[]> {
        return this.http.get<Post[]>(`${environment.apiUrl}/posts/subscribed`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }

    getPostById(postId: number): Observable<Post> {
        return this.http.get<Post>(`${environment.apiUrl}/posts/${postId}`).pipe(
            tap((post) => {
                console.log('Post récupéré :', post);
            }),
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }

    getCommentsForPost(postId: number): Observable<CommentInterfaces[]> {
        return this.http.get<CommentInterfaces[]>(`${environment.apiUrl}/posts/${postId}/comments`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }

    createMessage(message: string, postId: number): Observable<void> {
        return this.http.post<void>(`${environment.apiUrl}/posts/${postId}/comments`, { message }).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }

    createPost(newPost: NewPost) {
        return this.http.post<CreatedPostResponse>(`${environment.apiUrl}/posts/create`, newPost).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    // Handle 401/403 errors
                }
                throw error;
            })
        );
    }
}