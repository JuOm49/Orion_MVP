import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';

import { BehaviorSubject, map, take } from 'rxjs';

import { Post } from '@pages/interfaces/Post.interface';
import { PostsService } from '@pages/services/posts.service';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})

export class PostsComponent implements OnInit {

  private postsBehaviorSubject = new BehaviorSubject<Post[]>([]);
  public posts$ = this.postsBehaviorSubject.asObservable();
  public sortByDateAscending: boolean = true;

  readonly labelsForInterface = {
    createPost: 'Créer un article',
    sortBy: 'Trier par'
  };

  constructor(private postsService: PostsService, private router: Router) { }

  ngOnInit(): void {
    this.loadPosts();
  }

  clickOnPost(postId: number): void {
    this.router.navigate(['/posts', postId]);
  }

  clickOnSortBy(): void {
    const currentPosts = this.postsBehaviorSubject.value;
    let sortedPosts: Post[] = [];
    
    // Toggle sorting order based on updatedAt date
    if (this.sortByDateAscending) {
      sortedPosts = [...currentPosts].sort((a, b) => new Date(a.updatedAt).getTime() - new Date(b.updatedAt).getTime());
      this.sortByDateAscending = false;
    } else {
      sortedPosts = [...currentPosts].sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime());    
      this.sortByDateAscending = true;
    }
    
    this.postsBehaviorSubject.next(sortedPosts);
  }

  private loadPosts(): void {
    this.postsService.getFeed().pipe(
      take(1),
      map((posts: Post[]) => {
        if(!posts) return [];
        // Sort posts by updatedAt date descending
        posts.sort((a, b) => new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime());
        posts.forEach(post => {
          post.content = post.content.substring(0, 300) + '...';
        });
        return posts;
      })
    ).subscribe(posts => {
      this.postsBehaviorSubject.next(posts);
    });
  }

}
