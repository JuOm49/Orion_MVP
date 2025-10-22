import { HttpClient } from "@angular/common/http";
import { Injectable } from "@angular/core";

import { catchError, Observable} from "rxjs";

import { Post } from "@pages/interfaces/Post.interface";
import { NewPost } from "@pages/interfaces/NewPost.interface";
import { CreatedPostResponse } from "@pages/interfaces/CreatedPostResponse.interface";
import { Comment as CommentInterfaces } from "@pages/interfaces/Comment.interface";

import { environment } from "@env/environment";

@Injectable({
    providedIn: 'root'
})
export class PostsService {
    constructor( private http: HttpClient ) { }

    getFeed(): Observable<Post[]> {
        return this.http.get<Post[]>(`${environment.apiUrl}/posts/subscribed`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error fetching subscribed posts for user:', error);
                }
                throw error;
            })
        );
    }

    getPostById(postId: number): Observable<Post> {
        return this.http.get<Post>(`${environment.apiUrl}/posts/${postId}`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error fetching post by ID:', error);
                }
                throw error;
            })
        );
    }

    getCommentsForPost(postId: number): Observable<CommentInterfaces[]> {
        return this.http.get<CommentInterfaces[]>(`${environment.apiUrl}/posts/${postId}/comments`).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error fetching comments:', error);
                }
                throw error;
            })
        );
    }

    createMessage(message: string, postId: number): Observable<void> {
        return this.http.post<void>(`${environment.apiUrl}/posts/${postId}/comments`, { message }).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error creating message:', error);
                }
                throw error;
            })
        );
    }

    createPost(newPost: NewPost) {
        return this.http.post<CreatedPostResponse>(`${environment.apiUrl}/posts/create`, newPost).pipe(
            catchError((error) => {
                if ([400, 401, 403].includes(error.status)) {
                    console.error('Error creating post:', error);
                }
                throw error;
            })
        );
    }
}