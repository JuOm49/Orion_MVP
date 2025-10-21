import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { AuthGuard } from '@shared/guards/auth.guard';
import { UnauthGuard } from '@shared/guards/unauth.guard';


const routes: Routes = [
  { path: 'home', loadChildren: () => import('@pages/components/home/home.module').then(m => m.HomeModule) },
  { path: 'profile', canActivate: [AuthGuard] , loadChildren: () => import('@pages/components/profile/profile.module').then(m => m.ProfileModule) },
  { path: 'register', canActivate: [UnauthGuard], loadChildren: () => import('@pages/components/register/register.module').then(m => m.RegisterModule) },
  { path: 'login', canActivate: [UnauthGuard], loadChildren: () => import('@pages/components/login/login.module').then(m => m.LoginModule) },
  { path: 'posts/create', canActivate: [AuthGuard], loadChildren: () => import('@pages/components/post-create/post-create.module').then(m => m.PostCreateModule) },
  { path: 'posts/:id', canActivate: [AuthGuard], loadChildren: () => import('@pages/components/post/post.module').then(m => m.PostModule) },
  { path: 'posts', canActivate: [AuthGuard], loadChildren: () => import('@pages/components/posts/posts.module').then(m => m.PostsModule) },
  { path: 'subjects/:id', canActivate: [AuthGuard], loadChildren: () => import('@pages/shared/components/subjects/subjects.module').then(m => m.SubjectsModule) },
  { path: 'subjects', canActivate: [AuthGuard], loadChildren: () => import('@pages/shared/components/subjects/subjects.module').then(m => m.SubjectsModule) },
  { path: '', canActivate: [AuthGuard], loadChildren: () => import('@pages/components/home/home.module').then(m => m.HomeModule) }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule],
})
export class AppRoutingModule {}
