import { Component, OnInit } from '@angular/core';
import { NgForm } from '@angular/forms';
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
  
  readonly labelsForInterface = {
    comments: 'Commentaires',
    contentComment: 'Contenu du commentaire',
    ph_inputComment: 'Écrire ici votre commentaire',
  };

  constructor(private route: ActivatedRoute, private postService: PostsService) { }

  ngOnInit(): void {
    const postId = Number(this.route.snapshot.params['id']);
    this.post$ = this.postService.getPostById(postId);
  }

  onMessageSubmit(form: NgForm): void {
    if (form.valid) {
      if(form.valid) {
        console.log(form.value.message);
      }
    }
  }

}
