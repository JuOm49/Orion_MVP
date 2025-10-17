import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

// consider a guard combined with canLoad / canActivate route option
// to manage unauthenticated user to access private routes
const routes: Routes = [
  { path: 'profile', loadChildren: () => import('@pages/components/profile/profile.module').then(m => m.ProfileModule) },
  { path: 'register', loadChildren: () => import('@pages/components/register/register.module').then(m => m.RegisterModule) },
  { path: 'login', loadChildren: () => import('@pages/components/login/login.module').then(m => m.LoginModule) },
  { path: 'posts/create', loadChildren: () => import('@pages/components/post-create/post-create.module').then(m => m.PostCreateModule) },
  { path: 'posts/:id', loadChildren: () => import('@pages/components/post/post.module').then(m => m.PostModule) },
  { path: 'posts', loadChildren: () => import('@pages/components/posts/posts.module').then(m => m.PostsModule) },
  { path: 'subjects/:id', loadChildren: () => import('@pages/shared/components/subjects/subjects.module').then(m => m.SubjectsModule) },
  { path: 'subjects', loadChildren: () => import('@pages/shared/components/subjects/subjects.module').then(m => m.SubjectsModule) },
  { path: '', loadChildren: () => import('@pages/components/home/home.module').then(m => m.HomeModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
