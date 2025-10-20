import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Post } from '@app/pages/interfaces/Post.interface';

import { PostsService } from '@pages/services/posts.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.scss']
})
export class PostComponent implements OnInit {
  
  post$!: Observable<Post>;
  commentForm!: FormGroup;
  
  readonly labelsForInterface = {
    comments: 'Commentaires',
    contentComment: 'Contenu du commentaire',
    ph_inputComment: 'Écrire ici votre commentaire',
  };

  constructor(
    private route: ActivatedRoute, 
    private postService: PostsService,
    private formBuilder: FormBuilder
  ) { }

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.params['id']);
    this.post$ = this.postService.getPostById(postId);
    this.initCommentForm();
  }

  private initCommentForm(): void {
    this.commentForm = this.formBuilder.group({
      message: ['', [Validators.required, Validators.minLength(3)]]
    });
  }

  onMessageSubmit(): void {
    if (this.commentForm.valid) {
      console.log(this.commentForm.value.message);
      this.commentForm.reset();
    }
  }

}
