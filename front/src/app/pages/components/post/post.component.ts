import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Comment as CommentInterface } from '@pages/interfaces/Comment.interface';
import { Post } from '@pages/interfaces/Post.interface';

import { PostsService } from '@pages/services/posts.service';
import { BehaviorSubject, Observable, switchMap, map } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {
  
  post$!: Observable<Post>;
  commentForm!: FormGroup;
  commentBehaviorSubject = new BehaviorSubject<CommentInterface[]>([]);
  postId!: number;
  useCommentSubject = false;

  readonly labelsForInterface = {
    comments: 'Commentaires',
    contentComment: 'Contenu du commentaire',
    ph_inputComment: 'Écrire ici votre commentaire',
  };

  constructor( private route: ActivatedRoute, private postsService: PostsService, private formBuilder: FormBuilder ) { }

  // Getter pour obtenir les commentaires depuis la bonne source
  // get comments$(): Observable<CommentInterface[]> {
  //   return this.useCommentSubject 
  //     ? this.commentBehaviorSubject.asObservable()
  //     : this.post$.pipe(map((post: Post) => post?.comments || []));
  // }

  ngOnInit(): void {
    this.postId = Number(this.route.snapshot.params['id']);
    this.post$ = this.postsService.getPostById(this.postId);
    this.initCommentForm();
  }

  private initCommentForm(): void {
    this.commentForm = this.formBuilder.group({
      message: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  onMessageSubmit(): void {
    if (this.commentForm.valid) {
      const message = this.commentForm.value.message;
      
      this.postsService.createMessage(message, this.postId).pipe(
        switchMap(() => this.postsService.getCommentsForPost(this.postId))
      ).subscribe({
        next: (comments: CommentInterface[]) => {
          console.log('Commentaire créé avec succès');
          this.useCommentSubject = true; // Basculer vers le BehaviorSubject
          this.commentBehaviorSubject.next(comments);
          this.commentForm.reset();
        },
        error: (error) => {
          console.error('Erreur lors de la création du commentaire:', error);
        }
      });
    }
  }

}
