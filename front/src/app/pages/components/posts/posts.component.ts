import { Component, OnInit } from '@angular/core';
import { PostsService } from '@app/pages/services/posts.service';
import { take } from 'rxjs';

@Component({
  selector: 'app-posts',
  templateUrl: './posts.component.html',
  styleUrls: ['./posts.component.scss']
})

export class PostsComponent implements OnInit {

  constructor(private postsService: PostsService) { }

  ngOnInit(): void {
    this.postsService.getAll().pipe(take(1)).subscribe(posts => {
      console.log(posts);
    });
  }

}
