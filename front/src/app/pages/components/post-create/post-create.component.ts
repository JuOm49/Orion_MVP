import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

import { Observable, pipe, take } from 'rxjs';

import { Subject as SubjectInterface } from '@pages/interfaces/Subject.interface';
import { SubjectsService } from '@pages/services/subjects.service';
import { PostsService } from '@pages/services/posts.service';
import { Router } from '@angular/router';
import { NewPost } from '@app/pages/interfaces/NewPost.interface';
import { CreatedPostResponse } from '@app/pages/interfaces/CreatedPostResponse.interface';

@Component({
  selector: 'app-post-create',
  templateUrl: './post-create.component.html',
  styleUrls: ['./post-create.component.scss']
})
export class PostCreateComponent implements OnInit {

  createPostForm!: FormGroup
  subjects$!: Observable<SubjectInterface[]>;

  readonly labelsForInterface = {
    profile: 'Créer un nouvel article',
    selectSubject: 'Sélectionner un thème',
    ph_title: 'Titre de l\'article',
    ph_content: 'Contenu de l\'article',
    submit: 'Créer'
  };

  constructor(private formBuilder: FormBuilder, private route: Router, private subjectService: SubjectsService, private postsService : PostsService) { }

  ngOnInit(): void {
  this.initCreatePostForm();
   this.subjects$ = this.subjectService.getAll();

  }

  onSubmitForm(): void {
    if(this.createPostForm.valid) {
      const request : NewPost = {
        subjectId : this.createPostForm.value.subjects,
        title : this.createPostForm.value.title,
        content : this.createPostForm.value.content
      }

      this.postsService.createPost(request).pipe(take(1)).subscribe({
        next: (response: CreatedPostResponse) => {
          console.log('Post created successfully:', response);
          this.route.navigate(['/posts']);
        }, error: (error) => {
          console.error('Error creating post:', error);
        }
      });
    }
    this.createPostForm.reset();
  }

  getFieldError(fieldName: 'title' | 'content'): string | null {
    const field = this.createPostForm.get(fieldName);
    if (!field || !field.touched) return null;

    if (field.hasError('required')) {
      switch (fieldName) {
        case 'title':
          return "Le titre est requis";
        case 'content':
          return "Le contenu est requis";
      }
    }
    if (fieldName === 'title' && field.hasError('maxlength')) {
      return "Le titre ne doit pas dépasser 200 caractères";
    }
    if (fieldName === 'title' && field.hasError('minlength')) {
      return "Le titre doit contenir au moins 2 caractères";
    }
    if (fieldName === 'content') {
      if (field.hasError('minlength')) return "Le contenu doit contenir au moins 10 caractères";
    }

    return null;
  }

  private initCreatePostForm(): void {
    this.createPostForm = this.formBuilder.group({
      subjects: [null, [Validators.required]],
      title: ['', [Validators.required, Validators.minLength(2), Validators.maxLength(200)]],
      content: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

}
